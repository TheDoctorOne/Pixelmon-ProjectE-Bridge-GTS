package me.mahmutkocas.pixelmon.gtsemc.shop;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;

public class PokemonReplicate {
    public static Pokemon generate(ShopEntry.PokemonData pokemonData) {
        PokemonSpec pokemonSpec = PokemonSpec.from("random");
        Pokemon pokemon = pokemonSpec.create();

        pokemon.setUUID(pokemonData.uuid);
        pokemon.setSpecies(EnumSpecies.getFromDex(pokemonData.dex),true);
        pokemon.setAbility(pokemonData.ability);
        pokemon.setNickname(pokemonData.nickname);
        pokemon.setLevel(pokemonData.level);
        pokemon.setNature(EnumNature.getNatureFromIndex(pokemonData.nature));
        pokemon.setAbility(pokemonData.ability);
        pokemon.setCaughtBall(EnumPokeballs.getFromIndex(pokemonData.ball));
        pokemon.setForm(pokemonData.form);
        pokemon.setShiny(pokemonData.isShiny);
        pokemon.setGrowth(EnumGrowth.getGrowthFromIndex(pokemonData.growth));

        pokemon.getIVs().set(StatsType.HP, pokemonData.hp);
        pokemon.getIVs().set(StatsType.Attack, pokemonData.attack);
        pokemon.getIVs().set(StatsType.Defence, pokemonData.defence);
        pokemon.getIVs().set(StatsType.SpecialAttack, pokemonData.specialAttack);
        pokemon.getIVs().set(StatsType.SpecialDefence, pokemonData.specialDefence);
        pokemon.getIVs().set(StatsType.Speed, pokemonData.speed);

        if(pokemonData.gender.equals("male"))
            pokemon.setGender(Gender.Male);
        if(pokemonData.gender.equals("female"))
            pokemon.setGender(Gender.Female);
        if(pokemonData.gender.equals("none"))
            pokemon.setGender(Gender.None);

        return pokemon;
    }
}
