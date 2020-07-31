package me.mahmutkocas.pixelmon.gtsemc.shop;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import me.mahmutkocas.pixelmon.gtsemc.GtsEmcMain;
import net.minecraft.entity.player.EntityPlayer;

import java.io.Serializable;
import java.util.UUID;

public class ShopEntry implements Serializable {
    private transient EntityPlayer player;
    private PokemonData pokemonData;
    private String playerName;
    private UUID playerUUID;
    private long EmcValue;
    private transient Pokemon pokemon;
    private long date;



    public ShopEntry(EntityPlayer player, long EmcValue, Pokemon pokemon, long date) {
        this.player = player;
        this.playerName = player.getName();
        this.playerUUID = player.getUniqueID();
        this.EmcValue = EmcValue;
        this.pokemon = pokemon;
        this.date = date;
        pokemonData = new PokemonData(pokemon);
    }

    public void assignVariables() {
        try {
            player = (EntityPlayer) GtsEmcMain.server.getEntityFromUuid(playerUUID);
            pokemon = PokemonReplicate.generate(pokemonData);
            System.out.println(player.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UUID playerUUID() {
        return playerUUID;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public long getEmcValue() {
        return EmcValue;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public long getDate() {
        return date;
    }

    public String getPlayerName() {
        return playerName;
    }

    public PokemonData getPokemonIvs() {
        return pokemonData;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    protected class PokemonData implements Serializable {
        // Properties
        protected int dex;
        protected String nickname;
        protected int level;
        protected int nature;
        protected String ability;
        protected int ball;
        protected int growth;
        protected boolean isShiny;
        protected int form;
        protected String gender;
        protected UUID uuid;


        // IVs
        protected int hp;
        protected int attack;
        protected int defence;
        protected int specialAttack;
        protected int specialDefence;
        protected int speed;
        PokemonData(Pokemon pokemon) {
            uuid = pokemon.getUUID();
            dex = pokemon.getSpecies().getNationalPokedexInteger();
            nickname = pokemon.getNickname();
            level = pokemon.getLevel();
            nature = pokemon.getNature().index;
            ability = pokemon.getAbilityName();
            ball = pokemon.getCaughtBall().getIndex();
            form = pokemon.getForm();
            isShiny = pokemon.isShiny();
            growth = pokemon.getGrowth().index;

            if(pokemon.getGender().equals(Gender.Male))
                gender = "male";
            else if(pokemon.getGender().equals(Gender.Female))
                gender = "female";
            else if(pokemon.getGender().equals(Gender.None))
                gender = "none";

            hp = pokemon.getIVs().get(StatsType.HP);
            attack = pokemon.getIVs().get(StatsType.Attack);
            defence = pokemon.getIVs().get(StatsType.Defence);
            specialAttack = pokemon.getIVs().get(StatsType.SpecialAttack);
            specialDefence = pokemon.getIVs().get(StatsType.SpecialDefence);
            speed = pokemon.getIVs().get(StatsType.Speed);
        }
    }
}
