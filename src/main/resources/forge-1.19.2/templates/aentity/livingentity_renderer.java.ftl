<#--
 # MCreator (https://mcreator.net/)
 # Copyright (C) 2012-2020, Pylo
 # Copyright (C) 2020-2022, Pylo, opensource contributors
 #
 # This program is free software: you can redistribute it and/or modify
 # it under the terms of the GNU General Public License as published by
 # the Free Software Foundation, either version 3 of the License, or
 # (at your option) any later version.
 #
 # This program is distributed in the hope that it will be useful,
 # but WITHOUT ANY WARRANTY; without even the implied warranty of
 # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 # GNU General Public License for more details.
 #
 # You should have received a copy of the GNU General Public License
 # along with this program.  If not, see <https://www.gnu.org/licenses/>.
 #
 # Additional permission for code generator templates (*.ftl files)
 #
 # As a special exception, you may create a larger work that contains part or
 # all of the MCreator code generator templates (*.ftl files) and distribute
 # that work under terms of your choice, so long as that work isn't itself a
 # template for code generation. Alternatively, if you modify or redistribute
 # the template itself, you may (at your option) remove this special exception,
 # which will cause the template and the resulting code generator output files
 # to be licensed under the GNU General Public License without this special
 # exception.
-->

<#-- @formatter:off -->

<#include "../procedures.java.ftl">

package ${package}.client.renderer;

<#assign shadowRadius = "this.shadowRadius = " + data.modelShadowSize + "f;">

public class ${name}Renderer extends GeoEntityRenderer<${name}Entity> {
  public ${name}Renderer(EntityRendererProvider.Context renderManager) {
     super(renderManager, new ${name}Model());
     ${shadowRadius}
     <#if data.mobModelGlowTexture?has_content>
     this.addLayer(new ${name}Layer(this));
     </#if>
  }

@Override
   public RenderType getRenderType(${name}Entity entity, float partialTicks, PoseStack stack,
		MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
		ResourceLocation textureLocation) {
        <#if data.visualScale??>
			<#if hasProcedure(data.visualScale)>
        		Level world = entity.level;
        		double x = entity.getX();
        		double y = entity.getY();
        		double z = entity.getZ();
        		float scale = (float) <@procedureOBJToNumberCode data.visualScale/>;
        		stack.scale(scale, scale, scale);
        	<#else>
        		stack.scale(${data.visualScale.getFixedValue()}f, ${data.visualScale.getFixedValue()}f, ${data.visualScale.getFixedValue()}f);
        	</#if>
		</#if>
		return RenderType.entityTranslucent(getTextureLocation(entity));
	}

<#if data.disableDeathRotation>
 @Override
	protected float getDeathMaxRotation(${name}Entity entityLivingBaseIn) {
		return 0.0F;
	} 
</#if>
}