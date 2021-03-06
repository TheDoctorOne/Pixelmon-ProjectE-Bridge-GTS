package me.mahmutkocas.pixelmon.gtsemc.shop;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Calendar;


public class Shop {
    private static ShopFileHandler shopFileHandler;
    private final ArrayList<ShopEntry> shopEntries;
    private static ArrayList<String> toPay;
    public Shop() {
        shopFileHandler = new ShopFileHandler();
        toPay = shopFileHandler.readToPay();
        shopEntries = shopFileHandler.readEntries();
    }

    public ITextComponent getEntryDetails(int index, ShopEntry shopEntry) {
        shopEntry.assignVariables();

        Pokemon pokemon = shopEntry.getPokemon();

        int attack = shopEntry.getPokemonData().attack;
        int defence = shopEntry.getPokemonData().defence;
        int hp = shopEntry.getPokemonData().hp;
        int specialDefence = shopEntry.getPokemonData().specialDefence;
        int specialAttack = shopEntry.getPokemonData().specialAttack;
        int speed = shopEntry.getPokemonData().speed;

        ITextComponent entry = new TextComponentString("\n" + (index+1) + ". " + EnumSpecies.getFromDex(shopEntry.getPokemonData().dex).getPokemonName());
        ITextComponent details = new TextComponentTranslation("Details",0);
        details.appendText("\nSeller: " + shopEntry.getPlayerName());
        details.appendText("\nEMC: " + shopEntry.getEmcValue()).getStyle().setColor(TextFormatting.AQUA);
        if(shopEntry.getPokemonData().isShiny)
            details.appendText("\nShiny").getStyle().setColor(TextFormatting.YELLOW);
        details.appendText("\nGender: " + shopEntry.getPokemonData().gender).getStyle().setColor(TextFormatting.AQUA);
        details.appendText("\nAbility: " + shopEntry.getPokemonData().ability).getStyle().setColor(TextFormatting.AQUA);
        details.appendText("\nNature: " + shopEntry.getPokemonData().nature).getStyle().setColor(TextFormatting.AQUA);
        details.appendText("\nAttack: " + attack).getStyle().setColor(TextFormatting.DARK_AQUA);
        details.appendText("\nDefence: " + defence).getStyle().setColor(TextFormatting.DARK_AQUA);
        details.appendText("\nHP: " + hp).getStyle().setColor(TextFormatting.DARK_AQUA);
        details.appendText("\nSpecial Attack: " + specialAttack).getStyle().setColor(TextFormatting.DARK_AQUA);
        details.appendText("\nSpecial Defence: " + specialDefence).getStyle().setColor(TextFormatting.DARK_AQUA);
        details.appendText("\nSpeed: " + speed).getStyle().setColor(TextFormatting.DARK_AQUA);
        details.appendText("\n==MOVES==").getStyle().setColor(TextFormatting.DARK_AQUA);

        Attack attacks[] = new Attack[4];
        if(shopEntry.getPokemonData().move1 != null)
            attacks[0] = new Attack(shopEntry.getPokemonData().move1);
        if(shopEntry.getPokemonData().move2 != null)
            attacks[1] = new Attack(shopEntry.getPokemonData().move2);
        if(shopEntry.getPokemonData().move3 != null)
            attacks[2] = new Attack(shopEntry.getPokemonData().move3);
        if(shopEntry.getPokemonData().move4 != null)
            attacks[3] = new Attack(shopEntry.getPokemonData().move4);

        int i=1;
        for(Attack a : attacks) {
            if(a != null)
                if(a.getMove() != null)
                    if (a.getMove().getAttackName() != null) {
                        details.appendText("\n" + i + ". " + a.getMove().getAttackName()).getStyle().setColor(TextFormatting.DARK_AQUA);
                        i++;
                    }
        }
        if(shopEntry.getPokemonData().isShiny)
            details.getStyle().setColor(TextFormatting.YELLOW);

        entry.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, details));

        return entry;
    }

    // Page starts from 1
    public ITextComponent getPage(int pageNum) {
        ITextComponent page = new TextComponentString("=== Page " + pageNum + " ===");
        page.getStyle().setColor(TextFormatting.RED);
        for(int i=10*(pageNum-1); i<10+10*(pageNum-1); i++) {
            if(i >= shopEntries.size())
                break;
            try {
                ITextComponent entry = getEntryDetails(i, shopEntries.get(i));
                entry.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gtsemc buy " + i + " " + shopEntries.get(i).getPokemon().getUUID().toString()));
                page.appendSibling(entry);
            } catch (Exception e) {e.printStackTrace();}
        }

        return page;
    }

    public boolean addShopEntry(EntityPlayer player, int slot, long emc) {
        PlayerPartyStorage partyStorage =  Pixelmon.storageManager.getParty(player.getUniqueID());
        boolean ret = createShopEntry(player, emc,partyStorage.get(slot), Calendar.getInstance().getTimeInMillis());
        if(ret)
            partyStorage.set(slot, null);
        return ret;
    }

    private boolean createShopEntry(EntityPlayer player, long EmcValue, Pokemon pokemon, long date) {
        if(pokemon == null || pokemon.isEgg())
            return false;
        shopEntries.add(new ShopEntry(player, EmcValue, pokemon, date));
        return shopFileHandler.writeEntries(shopEntries);
    }

    // Player buys - event
    public boolean sellShopEntry(EntityPlayer player, int index, String uuid) {
        ShopEntry entry = shopEntries.get(index);
        IKnowledgeProvider provider = player.getCapability(ProjectEAPI.KNOWLEDGE_CAPABILITY, null);
        boolean ret;
        if(provider == null)
            return false;
        if(entry.getEmcValue() + 1 > provider.getEmc()) {
            return false;
        }
        else {
            entry.assignVariables();
            if(entry.getPokemon().getUUID().toString().equals(uuid)) {
                ret = removeEntry(player, index);
            }
            else
                return false;
            provider.setEmc(provider.getEmc() - entry.getEmcValue());

            if(entry.getPlayer() == null) {
                System.out.println("CAN'T GIVE EMC AMOUNT OF = " + entry.getEmcValue() + " TO " + entry.getPlayerName());
                toPay.add(entry.getPlayerName() + "," + entry.getEmcValue());
                shopFileHandler.writeToPay(toPay);
            } else {
                IKnowledgeProvider provider2 = entry.getPlayer().getCapability(ProjectEAPI.KNOWLEDGE_CAPABILITY, null);
                if(provider2 == null) {
                    System.out.println("Couldn't pay to " + entry.getPlayer().getName());
                    return false;
                }
                provider2.setEmc(provider2.getEmc() + entry.getEmcValue());
                ITextComponent mes = new TextComponentString("Your " + entry.getPokemon().getSpecies().getPokemonName() + " has been sold for " + entry.getEmcValue() +".");
                mes.getStyle().setColor(TextFormatting.AQUA);
                entry.getPlayer().sendMessage(mes);
            }
            if(provider.getEmc() <= 0) {
                provider.setEmc(1);
            }
            return ret;
        }
    }

    public ITextComponent getMyListings(EntityPlayer player) {
        ITextComponent res = new TextComponentString("=== MY LIST ===");
        res.getStyle().setColor(TextFormatting.RED);
        for(ShopEntry tmp : shopEntries) {
            if(tmp.getPlayer().equals(player)) {
                int i = shopEntries.indexOf(tmp);
                String uuid = tmp.getPokemon().getUUID().toString();
                ITextComponent entryDetails = getEntryDetails(i, tmp);
                entryDetails.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gtsemc remove " + i + " " + uuid));
                res.appendSibling(entryDetails);
            }
        }
        return res;
    }

    public boolean removeMyListing(EntityPlayer player, int index, String uuid) {
        if(shopEntries.get(index).getPlayerUUID().equals(player.getUniqueID()) && shopEntries.get(index).getPokemon().getUUID().toString().equals(uuid)) {
            return removeEntry(player, index);
        }

        return false;
    }

    private boolean removeEntry(EntityPlayer player, int index) {
        ArrayList<ShopEntry> toRemove = new ArrayList<>();
        toRemove.add(shopEntries.get(index));
        PlayerPartyStorage partyStorage = Pixelmon.storageManager.getParty(player.getUniqueID());
        partyStorage.add(shopEntries.get(index).getPokemon());
        shopEntries.removeAll(toRemove);
        shopFileHandler.writeEntries(shopEntries);

        return true;
    }


    public static class onJoin {
        @SubscribeEvent
        public static void onPlayerJoin(EntityJoinWorldEvent event) {
            if(event.getEntity() instanceof EntityPlayerMP) {
                System.out.println("Player Join Event");
                EntityPlayer player = (EntityPlayer) event.getEntity();
                ArrayList<String> toRemove = new ArrayList<>();
                for(String tmp : toPay) {
                    String[] splited = tmp.split(",");
                    String playerName = splited[0];
                    if(player.getName().equals(playerName)) {
                        long EMC = Long.parseLong(splited[1]);
                        IKnowledgeProvider provider = player.getCapability(ProjectEAPI.KNOWLEDGE_CAPABILITY, null);
                        if(provider != null)
                            provider.setEmc(provider.getEmc() + EMC);
                        else
                            player.sendMessage(new TextComponentString("Your pokemon sold but, can't pay you, contact admins."));
                        player.sendMessage(new TextComponentString("Your pokemon sold for " + EMC + " when you're away!"));
                        System.out.println("To Pay Job for the " + playerName + " for " + EMC + " is done.");
                        toRemove.add(tmp);
                    }
                }
                toPay.removeAll(toRemove);
                shopFileHandler.writeToPay(toPay);
            }
        }
    }



}
