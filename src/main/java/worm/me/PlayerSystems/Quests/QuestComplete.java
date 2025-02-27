package worm.me.PlayerSystems.Quests;

public class QuestComplete {

        public NamespacedKey daily1progress = new NamespacedKey("10plugin", "daily1progress");
        public NamespacedKey daily2progress = new NamespacedKey("10plugin", "daily2progress");
        public NamespacedKey daily3progress = new NamespacedKey("10plugin", "daily3progress");

        public void questProgress(NamespacedKey questSlot, NamespacedKey questProg, Player p){
            PersistentDataContainer container = p.getPersistentDataContainer();
            int currentProgress = container.getOrDefault(questProg, PersistentDataType.INTEGER, 0);
            int newProgress = currentProgress + 1;
            container.set(questProg, PersistentDataType.INTEGER, newProgress);

            // Get questslot as int to pull info from quest list
            int Quest = container.get(questSlot, PersistentDataType.INTEGER);

            if (newProgress == parseInt(QuestInitialize.dailyQuestAttributes.get(Quest).get(2))) {
                questComplete();
            }

        }

        public void questComplete(){

        }

        public void questClaim(){

        }
    
}
