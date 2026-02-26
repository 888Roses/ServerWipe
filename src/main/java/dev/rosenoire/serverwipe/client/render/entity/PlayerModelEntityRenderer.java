package dev.rosenoire.serverwipe.client.render.entity;

import dev.rosenoire.serverwipe.client.render.model.PlayerModelEntityModel;
import dev.rosenoire.serverwipe.common.entity.PlayerModelEntity;
import dev.rosenoire.serverwipe.foundation.role.BakedRoleModel;
import net.collectively.geode.math.math;
import net.collectively.geode.types.double2;
import net.collectively.geode.types.double3;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.BoneSnapshots;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.RenderPassInfo;

public class PlayerModelEntityRenderer<R extends EntityRenderState & GeoRenderState> extends GeoEntityRenderer<PlayerModelEntity, @NonNull R> {
    //region construction
    public PlayerModelEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new PlayerModelEntityModel());
    }

    @Override
    @SuppressWarnings("unchecked")
    public R createRenderState(PlayerModelEntity animatable, @Nullable Void relatedObject) {
        return (R) new RenderState();
    }
    //endregion

    //region access
    private static boolean isFirstPerson(PlayerModelEntity entity) {
        return entity.player != null && entity.player.isMainPlayer() && MinecraftClient.getInstance().options.getPerspective().isFirstPerson();
    }
    //endregion

    @Override
    public void addRenderData(PlayerModelEntity entity, @Nullable Void relatedObject, @NonNull R renderState, float delta) {
        RenderState state = (RenderState) renderState;

        if (entity.player != null) {
            if (!state.hasAdjustedModel) {
                if (getGeoModel() instanceof DefaultedEntityGeoModel<PlayerModelEntity> defaultedModel) {
                    entity.role().ifPresent(role -> {
                        BakedRoleModel bakedRoleModel = role.bakedRoleModel();
                        defaultedModel.withAltModel(bakedRoleModel.baseModelName());
                        defaultedModel.withAltAnimations(bakedRoleModel.baseAnimationName());
                        defaultedModel.withAltTexture(bakedRoleModel.baseTextureName());
                        state.hasAdjustedModel = true;
                    });
                }
            }
        }

        state.updateLerpedPlayerCoordinates(
                entity.lastPlayerPosition, entity.playerPosition,
                entity.lastPlayerRotation, entity.playerRotation,
                entity.lastPlayerRotationDifference, entity.playerRotationDifference,
                delta
        );

        state.isFirstPerson = isFirstPerson(entity);
    }

    @Override
    public void preRenderPass(@NonNull RenderPassInfo<@NonNull R> renderPassInfo, @NonNull OrderedRenderCommandQueue renderTasks) {
        RenderState renderState = (RenderState) renderPassInfo.renderState();
        MatrixStack matrices = renderPassInfo.poseStack();

        matrices.translate(renderState.lerpedPlayerPosition.sub(renderState.x, renderState.y, renderState.z).toVec3d());
    }

    @Override
    public void adjustRenderPose(@NonNull RenderPassInfo<@NonNull R> renderPassInfo) {
        RenderState renderState = (RenderState) renderPassInfo.renderState();
        MatrixStack matrices = renderPassInfo.poseStack();

        double rotationYaw = renderState.lerpedPlayerRotation.y();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f - (float) rotationYaw));
    }

    @Override
    public void adjustModelBonesForRender(@NonNull RenderPassInfo<@NonNull R> renderPassInfo, @NonNull BoneSnapshots snapshots) {
        super.adjustModelBonesForRender(renderPassInfo, snapshots);
        RenderState renderState = (RenderState) renderPassInfo.renderState();

        float targetScale = renderState.isFirstPerson ? 0 : 1;

        snapshots.get("head").ifPresent(x -> {
            float pitch = (float) math.deg2rad(-renderState.lerpedPlayerRotation.x());
            float difference = (float) math.deg2rad(renderState.lerpedPlayerRotationDifference.y() * 0.25);

            x.setRotation(pitch, -difference, 0);
            x.setScale(targetScale, targetScale, targetScale);
        });

        snapshots.get("body").ifPresent(x -> x.setScale(targetScale, targetScale, targetScale));

        // Hiding the testing figurine.
        snapshots.get("all").ifPresent(x -> x.setScale(0, 0, 0));
    }

    @Override
    public boolean shouldRender(PlayerModelEntity entity, Frustum frustum, double x, double y, double z) {
        return super.shouldRender(entity, frustum, x, y, z) && entity.roleHolder().map(v -> v.role().isPresent()).orElse(false);
    }

    public static class RenderState extends EntityRenderState {
        public double3 lerpedPlayerPosition = new double3(0);
        public double2 lerpedPlayerRotation = new double2(0); // pitch, yaw
        public double2 lerpedPlayerRotationDifference = new double2(0);
        public boolean isFirstPerson;
        public boolean hasAdjustedModel;

        public void updateLerpedPlayerCoordinates(@Nullable double3 lastPosition,
                                                  @Nullable double3 position,
                                                  @Nullable double2 lastRotation,
                                                  @Nullable double2 rotation,
                                                  @Nullable double2 lastPlayerRotationDifference,
                                                  @Nullable double2 playerRotationDifference,
                                                  double delta) {
            if (lastPosition != null && position != null) {
                lerpedPlayerPosition = lastPosition.lerp(delta, position);
            }

            if (lastRotation != null && rotation != null) {
                lerpedPlayerRotation = lastRotation.lerp(delta, rotation);
            }

            if (lastPlayerRotationDifference != null && playerRotationDifference != null) {
                lerpedPlayerRotationDifference = lastPlayerRotationDifference.lerp(delta, playerRotationDifference);
            }
        }
    }
}
