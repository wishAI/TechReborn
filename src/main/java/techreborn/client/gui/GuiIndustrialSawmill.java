/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.client.gui;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import reborncore.ClientProxy;
import reborncore.client.gui.builder.GuiBase;
import reborncore.client.gui.builder.TRBuilder;
import reborncore.client.multiblock.Multiblock;
import reborncore.client.multiblock.MultiblockRenderEvent;
import reborncore.client.multiblock.MultiblockSet;
import techreborn.blocks.BlockMachineCasing;
import reborncore.client.gui.builder.widget.GuiButtonHologram;
import techreborn.init.ModBlocks;
import techreborn.tiles.multiblock.TileIndustrialSawmill;

import java.io.IOException;

public class GuiIndustrialSawmill extends GuiBase {

	TileIndustrialSawmill tile;

	public GuiIndustrialSawmill(final EntityPlayer player, final TileIndustrialSawmill tile) {
		super(player, tile, tile.createContainer(player));
		this.tile = tile;
	}

	@Override
	public void initGui() {
		super.initGui();
		ClientProxy.multiblockRenderEvent.setMultiblock(null);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		final Layer layer = Layer.BACKGROUND;
		
		// Battery slot
		this.drawSlot(8, 72, layer);
		// Liquid input slot
		this.drawSlot(34, 35, layer);
		// Liquid output slot
		this.drawSlot(34, 55, layer);
		// Solid material input slot
		this.drawSlot(84, 43, layer);
		// Output slots
		this.drawSlot(126, 25, layer);
		this.drawSlot(126, 43, layer);
		this.drawSlot(126, 61, layer);
		
		this.builder.drawJEIButton(this, 158, 5, layer);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		final Layer layer = Layer.FOREGROUND;
		
		this.builder.drawProgressBar(this, this.tile.getProgressScaled(100), 100, 105, 47, mouseX, mouseY, TRBuilder.ProgressDirection.RIGHT, layer);
		this.builder.drawTank(this, 53, 25, mouseX, mouseY, this.tile.tank.getFluid(), this.tile.tank.getCapacity(), this.tile.tank.isEmpty(), layer);
		this.builder.drawMultiEnergyBar(this, 9, 19, (int) this.tile.getEnergy(), (int) this.tile.getMaxPower(), mouseX, mouseY, 0, layer);
		if (tile.getMutliBlock()) {
			addHologramButton(6, 4, 212, layer);
			builder.drawHologramButton(this, 6, 4, mouseX, mouseY, layer);
		} else {
			builder.drawMultiblockMissingBar(this, layer);
			addHologramButton(76, 56, 212, layer);
			builder.drawHologramButton(this, 76, 56, mouseX, mouseY, layer);
		}	
	}
	
	public void addHologramButton(int x, int y, int id, Layer layer) {
		int factorX = 0;
		int factorY = 0;
		if (layer == Layer.BACKGROUND) {
			factorX = guiLeft;
			factorY = guiTop;
		}
		buttonList.add(new GuiButtonHologram(id, x + factorX, y + factorY, this, layer));
	}
	
	@Override
	public void actionPerformed(final GuiButton button) throws IOException {
		super.actionPerformed(button);
		if (button.id == 212 && GuiBase.slotConfigType == SlotConfigType.NONE) {
			if (ClientProxy.multiblockRenderEvent.currentMultiblock == null) {
				{
					// This code here makes a basic multiblock and then sets to the selected one.
					final Multiblock multiblock = new Multiblock();
					IBlockState standardCasing = ModBlocks.MACHINE_CASINGS.getDefaultState().withProperty(BlockMachineCasing.TYPE, "standard");
					IBlockState reinforcedCasing = ModBlocks.MACHINE_CASINGS.getDefaultState().withProperty(BlockMachineCasing.TYPE, "reinforced");
					
					this.addComponent(0, -1, 0, standardCasing, multiblock);
					this.addComponent(1, -1, 0, standardCasing, multiblock);
					this.addComponent(0, -1, 1, standardCasing, multiblock);
					this.addComponent(-1, -1, 0, standardCasing, multiblock);
					this.addComponent(0, -1, -1, standardCasing, multiblock);
					this.addComponent(-1, -1, -1, standardCasing, multiblock);
					this.addComponent(-1, -1, 1, standardCasing, multiblock);
					this.addComponent(1, -1, -1, standardCasing, multiblock);
					this.addComponent(1, -1, 1, standardCasing, multiblock);

					this.addComponent(0, 0, 0, Blocks.WATER.getDefaultState(), multiblock);
					this.addComponent(1, 0, 0, reinforcedCasing, multiblock);
					this.addComponent(0, 0, 1, reinforcedCasing, multiblock);
					this.addComponent(-1, 0, 0, reinforcedCasing, multiblock);
					this.addComponent(0, 0, -1, reinforcedCasing, multiblock);
					this.addComponent(-1, 0, -1, reinforcedCasing, multiblock);
					this.addComponent(-1, 0, 1, reinforcedCasing, multiblock);
					this.addComponent(1, 0, -1, reinforcedCasing, multiblock);
					this.addComponent(1, 0, 1, reinforcedCasing, multiblock);

					this.addComponent(0, 1, 0, standardCasing, multiblock);
					this.addComponent(0, 1, 0, standardCasing, multiblock);
					this.addComponent(1, 1, 0, standardCasing, multiblock);
					this.addComponent(0, 1, 1, standardCasing, multiblock);
					this.addComponent(-1, 1, 0, standardCasing, multiblock);
					this.addComponent(0, 1, -1, standardCasing, multiblock);
					this.addComponent(-1, 1, -1, standardCasing, multiblock);
					this.addComponent(-1, 1, 1, standardCasing, multiblock);
					this.addComponent(1, 1, -1, standardCasing, multiblock);
					this.addComponent(1, 1, 1, standardCasing, multiblock);

					final MultiblockSet set = new MultiblockSet(multiblock);
					ClientProxy.multiblockRenderEvent.setMultiblock(set);
					ClientProxy.multiblockRenderEvent.parent = this.tile.getPos();
					MultiblockRenderEvent.anchor = new BlockPos(
							this.tile.getPos().getX()
									- EnumFacing.getFront(this.tile.getFacingInt()).getFrontOffsetX() * 2,
							this.tile.getPos().getY() - 1, this.tile.getPos().getZ()
									- EnumFacing.getFront(this.tile.getFacingInt()).getFrontOffsetZ() * 2);
				}
			} else {
				ClientProxy.multiblockRenderEvent.setMultiblock(null);
			}
		}
	}

	public void addComponent(final int x, final int y, final int z, final IBlockState blockState, final Multiblock multiblock) {
		multiblock.addComponent(new BlockPos(x, y, z), blockState);
	}
}
