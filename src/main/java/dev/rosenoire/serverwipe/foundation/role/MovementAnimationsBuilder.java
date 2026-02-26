package dev.rosenoire.serverwipe.foundation.role;

public class MovementAnimationsBuilder {
    private String walk = "move.walk";
    private String startWalk = "move.walk.enter";
    private String exitWalk = "move.walk.exit";
    private String run = "move.run";
    private String startRun = "move.run.enter";
    private String exitRun = "move.run.exit";
    private String fall = "fall";
    private String airborne = "airborne";
    private String land = "land";
    private String idle = "idle";

    public static MovementAnimationsBuilder builder() {
        return new MovementAnimationsBuilder();
    }

    private MovementAnimationsBuilder() {
    }

    public MovementAnimations build() {
        return new MovementAnimations(
                walk, startWalk, exitWalk,
                run, startRun, exitRun,
                airborne, fall, land,
                idle
        );
    }

    public MovementAnimationsBuilder withWalk(String walk) {
        this.walk = walk;
        return this;
    }

    public MovementAnimationsBuilder withStartWalk(String startWalk) {
        this.startWalk = startWalk;
        return this;
    }

    public MovementAnimationsBuilder withExitWalk(String exitWalk) {
        this.exitWalk = exitWalk;
        return this;
    }

    public MovementAnimationsBuilder withRun(String run) {
        this.run = run;
        return this;
    }

    public MovementAnimationsBuilder withStartRun(String startRun) {
        this.startRun = startRun;
        return this;
    }

    public MovementAnimationsBuilder withExitRun(String exitRun) {
        this.exitRun = exitRun;
        return this;
    }

    public MovementAnimationsBuilder withFall(String fall) {
        this.fall = fall;
        return this;
    }

    public MovementAnimationsBuilder withAirborne(String airborne) {
        this.airborne = airborne;
        return this;
    }

    public MovementAnimationsBuilder withLand(String land) {
        this.land = land;
        return this;
    }

    public MovementAnimationsBuilder withIdle(String idle) {
        this.idle = idle;
        return this;
    }
}
