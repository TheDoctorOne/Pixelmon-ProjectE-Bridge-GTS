package me.mahmutkocas.pixelmon.gtsemc;

import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.sun.org.apache.xpath.internal.operations.Gt;
import me.mahmutkocas.pixelmon.gtsemc.shop.Shop;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.gameObjs.container.TransmutationContainer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import scala.Int;

import javax.swing.text.html.parser.Entity;
import java.util.Calendar;


public class Command extends CommandBase {
    protected String help = "Commands\n/gtsemc - Pokemon List\n/gtsemc page 2 - Page 2 of the List\n/gtsemc sell <slot> <EMC>\n/gtsemc mylist - Your Listings";
    @Override
    public String getName() {
        return "gtsemc";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return help;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        if(iCommandSender.getCommandSenderEntity() instanceof EntityPlayer) {
            Shop shop = GtsEmcMain.shop;
            EntityPlayer player = (EntityPlayer) iCommandSender.getCommandSenderEntity();

            if (strings.length < 1) {
                int page = 1;
                iCommandSender.sendMessage(shop.getPage(page));
            } else if (strings[0].equals("page")) {
                if(strings.length < 2) {
                    iCommandSender.sendMessage(new TextComponentString("/gtsemc page <number>"));
                    return;
                }
                int page = Integer.parseInt(strings[1]);
                iCommandSender.sendMessage(shop.getPage(page));
            } else if (strings[0].equals("buy")) {
                // index - uuid
                int index = Integer.parseInt(strings[1]);
                String uuid = strings[2];

                if(!shop.sellShopEntry(player, index, uuid)) {
                    ITextComponent fail = new TextComponentString("Failed to buy.");
                    fail.getStyle().setColor(TextFormatting.RED);
                    iCommandSender.sendMessage(fail);
                } else {
                    ITextComponent success = new TextComponentString("Successfully bought.");
                    success.getStyle().setColor(TextFormatting.AQUA);
                    iCommandSender.sendMessage(success);
                }
            } else if (strings[0].equals("sell")) {
                if(strings.length < 3) {
                    iCommandSender.sendMessage(new TextComponentString("/gtsemc sell <slot> <EMC>"));
                    return;
                }
                int slot = Integer.parseInt(strings[1]);
                long EMC = Long.parseLong(strings[2].replaceAll("\\.",""));

                if(!shop.addShopEntry(player,slot-1,EMC)) {
                    ITextComponent fail = new TextComponentString("Failed to add.");
                    fail.getStyle().setColor(TextFormatting.RED);
                    iCommandSender.sendMessage(fail);
                } else {
                    ITextComponent success = new TextComponentString("Successfully added for sell.");
                    success.getStyle().setColor(TextFormatting.AQUA);
                    iCommandSender.sendMessage(success);
                }
            } else if (strings[0].equals("mylist")) {
                iCommandSender.sendMessage(shop.getMyListings(player));
            } else if (strings[0].equals("remove")) {
                int index = Integer.parseInt(strings[1]);
                String uuid = strings[2];
                if(!shop.removeMyListing(player, index, uuid)) {
                    ITextComponent fail = new TextComponentString("Failed to remove.");
                    fail.getStyle().setColor(TextFormatting.RED);
                    iCommandSender.sendMessage(fail);
                } else {
                    ITextComponent success = new TextComponentString("Successfully removed.");
                    success.getStyle().setColor(TextFormatting.AQUA);
                    iCommandSender.sendMessage(success);
                }
            } else if (strings[0].equals("help")) {
                iCommandSender.sendMessage(new TextComponentString(help));
            }
        }
    }
}
