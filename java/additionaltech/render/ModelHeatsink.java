package additionaltech.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelHeatsink extends ModelBase {
  //fields
    ModelRenderer base;
    ModelRenderer end_fin1;
    ModelRenderer end_fin2;
    ModelRenderer fin[];
    
  
  public ModelHeatsink()
  {
    textureWidth = 64;
    textureHeight = 32;
    this.fin = new ModelRenderer[14];
    
      base = new ModelRenderer(this, 0, 0);
      base.addBox(-8F, 0F, -8F, 16, 2, 16);
      base.setRotationPoint(0F, 22F, 0F);
      base.setTextureSize(64, 32);
      base.mirror = true;
      setRotation(base, -0.0185893F, 0F, 0F);
      end_fin1 = new ModelRenderer(this, 16, 0);
      end_fin1.addBox(0F, 0F, 0F, 16, 14, 1);
      end_fin1.setRotationPoint(-8F, 22F, 8F);
      end_fin1.setTextureSize(64, 32);
      end_fin1.mirror = true;
      setRotation(end_fin1, 3.141593F, 0F, 0F);
      end_fin2 = new ModelRenderer(this, 16, 0);
      end_fin2.addBox(0F, 0F, 0F, 16, 14, 1);
      end_fin2.setRotationPoint(-8F, 22F, -7F);
      end_fin2.setTextureSize(64, 32);
      end_fin2.mirror = true;
      setRotation(end_fin2, 3.141593F, 0F, 0F);
      for (int i = 0; i < 14; ++i) {
    	  fin[i] = new ModelRenderer(this, 16, 0);
    	  fin[i].addBox(0F, 0F, 0F, 16, 14, 0);
    	  fin[i].setRotationPoint(-8F, 22F, -6F + i);
    	  fin[i].setTextureSize(64, 32);
    	  fin[i].mirror = true;
    	  setRotation(fin[i], 3.141593F, 0F, 0F);
      }
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    base.render(f5);
    end_fin1.render(f5);
    end_fin2.render(f5);
    for (int i = 0; i < 14; ++i) {
    	fin[i].render(f5);
    }
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
  }

}
