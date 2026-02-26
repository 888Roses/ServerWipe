package dev.rosenoire.serverwipe.common.entity;

import dev.rosenoire.serverwipe.api.entity.AbstractGenericEntity;
import dev.rosenoire.serverwipe.cca.RoleHolderComponent;
import dev.rosenoire.serverwipe.common.ServerWipe;
import dev.rosenoire.serverwipe.common.index.ModEntityComponents;
import dev.rosenoire.serverwipe.foundation.role.MovementAnimations;
import dev.rosenoire.serverwipe.foundation.role.Role;
import net.collectively.geode.debug.Draw;
import net.collectively.geode.types.double2;
import net.collectively.geode.types.double3;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.Uuids;
import net.minecraft.world.World;
import org.jspecify.annotations.NonNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.object.LoopType;
import software.bernie.geckolib.animation.object.PlayState;
import software.bernie.geckolib.animation.state.AnimationTest;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

public class PlayerModelEntity extends AbstractGenericEntity implements GeoEntity {
    public PlayerEntity player;

    public double2 lastPlayerRotation = new double2(0);
    public double2 playerRotation = new double2(0);
    public double2 lastPlayerRotationDifference = new double2(0);
    public double2 playerRotationDifference = new double2(0);
    public double3 lastPlayerPosition = double3.zero;
    public double3 playerPosition = double3.zero;
    public boolean isSprinting;
    public boolean wasSprinting;
    public boolean isGrounded;
    public boolean wasGrounded;
    public double fallDistance;
    public double lastFallDistance;

    //region player
    public void setPlayer(PlayerEntity player) {
        this.player = player;
    }

    private void readPlayer(ReadView readView) {
        readView.read("player", Uuids.INT_STREAM_CODEC).ifPresent(uuid -> {
            World world = world();

            if (world == null) {
                return;
            }

            player = world.getPlayerByUuid(uuid);
        });
    }

    private void writePlayer(WriteView writeView) {
        if (player != null) {
            writeView.put("player", Uuids.INT_STREAM_CODEC, player.getUuid());
        }
    }
    //endregion

    //region construction
    public PlayerModelEntity(EntityType<?> type, World world) {
        super(type, world);
    }
    //endregion

    //region data tracker
    @Override
    protected void initDataTracker(DataTracker.Builder builder) {

    }
    //endregion

    //region override
    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return false;
    }
    //endregion

    //region data
    @Override
    protected void readCustomData(ReadView readView) {
        readPlayer(readView);

        lastPlayerRotation = readView.read("lastPlayerRotation", double2.CODEC).orElse(new double2(0));
        playerRotation = readView.read("playerRotation", double2.CODEC).orElse(new double2(0));

        lastPlayerRotationDifference = readView.read("lastPlayerRotationDifference", double2.CODEC).orElse(new double2(0));
        playerRotationDifference = readView.read("playerRotationDifference", double2.CODEC).orElse(new double2(0));

        lastPlayerPosition = readView.read("lastPlayerPosition", double3.CODEC).orElse(double3.zero);
        playerPosition = readView.read("playerPosition", double3.CODEC).orElse(double3.zero);
    }

    @Override
    protected void writeCustomData(WriteView writeView) {
        writePlayer(writeView);

        writeView.put("lastPlayerRotation", double2.CODEC, lastPlayerRotation);
        writeView.put("playerRotation", double2.CODEC, playerRotation);

        writeView.put("lastPlayerRotationDifference", double2.CODEC, lastPlayerRotationDifference);
        writeView.put("playerRotationDifference", double2.CODEC, playerRotationDifference);

        writeView.put("lastPlayerPosition", double3.CODEC, lastPlayerPosition);
        writeView.put("playerPosition", double3.CODEC, playerPosition);
    }
    //endregion

    //region tick
    @Override
    public void tick() {
        super.tick();

        Draw.text("Has Player: " + (player != null), new double3(0, -0.5, 0).addY(world().isClient() ? 0 : 0.25), world().isClient() ? 0xffffff55 : 0xff55ffff);

        // Cannot exist without a player.
        if (player == null) {
            // discard();
            return;
        }

        lastPlayerRotation = playerRotation;
        playerRotation = new double2(player.getPitch(), player.getYaw());

        lastPlayerRotationDifference = playerRotationDifference;
        playerRotationDifference = playerRotation.sub(lastPlayerRotation);

        lastPlayerPosition = playerPosition;
        playerPosition = new double3(player.getEntityPos());

        wasSprinting = isSprinting;
        isSprinting = player.isSprinting();

        wasGrounded = isGrounded;
        isGrounded = player.isOnGround();

        lastFallDistance = fallDistance;
        fallDistance = player.fallDistance;

        setPosition(playerPosition.toVec3d());
        setRotation((float) playerRotation.x(), (float) playerRotation.y());
    }
    //endregion

    //region access
    public boolean isMoving() {
        return playerPosition.sub(lastPlayerPosition).squaredHorMag() > 0.01;
    }

    public Optional<RoleHolderComponent> roleHolder() {
        return Optional.ofNullable(player).map(player -> player.getComponent(ModEntityComponents.ROLE_HOLDER));
    }

    public Optional<Role> role() {
        return roleHolder().flatMap(RoleHolderComponent::role);
    }
    //endregion

    //region animation

    //region cache
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public @NonNull AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
    //endregion

    //region handlers
    public static final class Handler implements AnimationController.AnimationStateHandler<PlayerModelEntity> {
        @Override
        public @NonNull PlayState handle(@NonNull AnimationTest<PlayerModelEntity> animation) {
            PlayerModelEntity playerModel = animation.animatable();

            return playerModel.roleHolder().flatMap(component -> component.role()
                    .map(role -> handleWithRole(animation, playerModel, component, role))
            ).orElse(PlayState.STOP);
        }

        private PlayState handleWithRole(@NonNull AnimationTest<PlayerModelEntity> animation, PlayerModelEntity playerModel, RoleHolderComponent component, Role role) {
            boolean isMoving = playerModel.isMoving();
            boolean isSprinting = playerModel.isSprinting;
            boolean wasFalling = !playerModel.wasGrounded;
            boolean isFalling = !playerModel.isGrounded;

            MovementAnimations movementAnimations = role.bakedRoleModel().movementAnimations();

            if (isFalling && playerModel.lastFallDistance > 1) {
                return animation.setAndContinue(RawAnimation.begin()
                        .then(movementAnimations.fall(), LoopType.HOLD_ON_LAST_FRAME)
                        .thenLoop(movementAnimations.airborne())
                );
            }

            if (wasFalling) {
                RawAnimation anim = RawAnimation.begin();

                if (playerModel.lastFallDistance > 1) {
                    anim.then(movementAnimations.land(), LoopType.HOLD_ON_LAST_FRAME);
                }

                if (isMoving) {
                    if (isSprinting) {
                        anim.then(movementAnimations.startRun(), LoopType.HOLD_ON_LAST_FRAME).thenLoop(movementAnimations.run());
                    } else {
                        anim.then(movementAnimations.startWalk(), LoopType.HOLD_ON_LAST_FRAME).thenLoop(movementAnimations.walk());
                    }
                } else {
                    anim.thenLoop(movementAnimations.idle());
                }

                return animation.setAndContinue(anim);
            }

            if (animation.isCurrentAnimationStage(movementAnimations.land())) {
                return PlayState.CONTINUE;
            }

            if (isMoving) {
                if (isSprinting) {
                    return animation.setAndContinue(RawAnimation.begin()
                            .then(movementAnimations.startRun(), LoopType.HOLD_ON_LAST_FRAME)
                            .thenLoop(movementAnimations.run())
                    );
                }

                return animation.setAndContinue(RawAnimation.begin()
                        .then(movementAnimations.startWalk(), LoopType.HOLD_ON_LAST_FRAME)
                        .thenLoop(movementAnimations.walk())
                );
            }

            return animation.setAndContinue(RawAnimation.begin().thenLoop(movementAnimations.idle()));
        }
    }
    //endregion

    //region controllers
    private final AnimationController<PlayerModelEntity> MOVEMENT_CONTROLLER = new AnimationController<>("movement", new Handler());

    @Override
    public void registerControllers(AnimatableManager.@NonNull ControllerRegistrar controllers) {
        controllers.add(MOVEMENT_CONTROLLER);
    }
    //endregion

    //endregion
}
