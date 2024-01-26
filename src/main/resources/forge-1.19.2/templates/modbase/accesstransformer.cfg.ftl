<#if w.hasElementsOfType("dimension")>
public net.minecraft.client.renderer.DimensionSpecialEffects f_108857_ #EFFECTS
</#if>

<#if w.hasElementsOfType("gamerule")>
public net.minecraft.world.level.GameRules$IntegerValue m_46312_(I)Lnet/minecraft/world/level/GameRules$Type; #create
public net.minecraft.world.level.GameRules$BooleanValue m_46250_(Z)Lnet/minecraft/world/level/GameRules$Type; #create
</#if>

<#if w.hasElementsOfType("biome")>
public net.minecraft.world.level.biome.MultiNoiseBiomeSource <init>(Lnet/minecraft/world/level/biome/Climate$ParameterList;Ljava/util/Optional;)V #constructor
public-f net.minecraft.world.level.biome.MultiNoiseBiomeSource f_48438_ #preset
public-f net.minecraft.world.level.biome.MultiNoiseBiomeSource f_48435_ #parameters
public-f net.minecraft.world.level.chunk.ChunkGenerator f_62137_ #biomeSource
public-f net.minecraft.world.level.chunk.ChunkGenerator f_223020_ #featuresPerStep
public-f net.minecraft.world.level.chunk.ChunkGenerator f_223021_ #generationSettingsGetter
public-f net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator f_64318_ #settings
public net.minecraft.world.level.levelgen.SurfaceRules$SequenceRuleSource
</#if>

<#if w.hasElementsOfType("feature")>
public net.minecraft.world.level.levelgen.feature.ScatteredOreFeature <init>(Lcom/mojang/serialization/Codec;)V #constructor
public-f net.minecraft.world.level.levelgen.feature.TreeFeature m_142674_(Lnet/minecraft/world/level/levelgen/feature/FeaturePlaceContext;)Z #place
</#if>

<#if w.hasElementsOfType("animateditem")>
public net.minecraft.client.renderer.ItemInHandRenderer f_109302_ # mainHandHeight
public net.minecraft.client.renderer.ItemInHandRenderer f_109303_ # oMainHandHeight
public net.minecraft.client.renderer.ItemInHandRenderer f_109304_ # offHandHeight
public net.minecraft.client.renderer.ItemInHandRenderer f_109305_ # oOffHandHeight
</#if>