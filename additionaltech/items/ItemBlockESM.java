package additionaltech.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

public class ItemBlockESM extends ItemBlockWithMetadata {

        public ItemBlockESM(Block block) {
                super(block, block);
        }
        
        @Override
        public String getUnlocalizedName(ItemStack stack) {
            switch (stack.getItemDamage()) {
            case 0:
                return this.getUnlocalizedName() + "I";
            case 1:
                return this.getUnlocalizedName() + "II";
            case 2:
                return this.getUnlocalizedName() + "III";
            default:
                return this.getUnlocalizedName();
            }
        }
}
