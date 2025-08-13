package worm.me.playersystems.data.levels;

public class Reqs {

    public static class RequirementBuilder {

        private final int[] skillLevels;

        public RequirementBuilder() {
            skillLevels = new int[Skill.values().length];
        }

        public int[] build() {
            int[] copy = new int[skillLevels.length];
            System.arraycopy(skillLevels, 0, copy, 0, skillLevels.length);
            return copy;
        }

        public RequirementBuilder setSkillLevel(Skill skill, int level) {
            skillLevels[skill.ordinal()] = level;
            return this;
        }

        public RequirementBuilder combat(int level) {
            return setSkillLevel(Skill.COMBAT, level);
        }

        public RequirementBuilder mining(int level) {
            return setSkillLevel(Skill.MINING, level);
        }

        public RequirementBuilder farming(int level) {
            return setSkillLevel(Skill.FARMING, level);
        }

        public RequirementBuilder fishing(int level) {
            return setSkillLevel(Skill.FISHING, level);
        }

        public RequirementBuilder foraging(int level) {
            return setSkillLevel(Skill.FORAGING, level);
        }

        public RequirementBuilder crafting(int level) {
            return setSkillLevel(Skill.CRAFTING, level);
        }

        public RequirementBuilder smithing(int level) {
            return setSkillLevel(Skill.SMITHING, level);
        }

        public RequirementBuilder agility(int level) {
            return setSkillLevel(Skill.AGILITY, level);
        }

    }
}