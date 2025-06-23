package worm.me.commands.legacy;

//public class LostToolsCommand implements CommandExecutor {
//    @Override
//    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
//
//        if (sender instanceof Player p){
//            Inventory inv = p.getInventory();
//
//            for (ItemStack item : VoidPlayer.getTools(p)){
//                if (inv.firstEmpty() != -1){
//                    if (!inv.contains(item.getType())) inv.addItem(item);
//                } else {
//                    Server.pluginMessage(ChatColor.RED + "You have no space in your inventory to receive your tools!", p);
//                    break;
//                }
//            } return true;
//
//        }
//
//        return false;
//    }
//}
