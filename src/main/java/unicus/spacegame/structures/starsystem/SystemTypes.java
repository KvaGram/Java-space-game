package unicus.spacegame.structures.starsystem;

enum SystemTypes {
    /**
     * This is a near clone of the Sol system.
     * Little is left to chance compared to an full recreation.
     * @see SolCloneSystem
     */
    solClone,
    /**
     * The Sol-like system generates a system with two sets of planets.
     * The inner set has 2 to 4 planets, 1 of them has life on it.
     * Between the sets, there is a asteroid belt.
     * the outer set has 1 to 4 gas planets.
     * At the system border, there is another thinner belt
     * @see SolLikeSystem
     */
    solLike
}
