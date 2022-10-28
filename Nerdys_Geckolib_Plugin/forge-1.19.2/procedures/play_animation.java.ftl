${input$entity}.getPersistentData().putString("animation", ${input$animation});
class GeckoAnim${customBlockIndex} {
			private int ticks = 0;
			private float waitTicks;
			private LevelAccessor world;

			public void start(LevelAccessor world, int waitTicks) {
				this.waitTicks = waitTicks;
				this.world = world;
				MinecraftForge.EVENT_BUS.register(GeckoAnim${customBlockIndex}.this);
			}

			@SubscribeEvent
			public void tick(TickEvent.ServerTickEvent event) {
				if (event.phase == TickEvent.Phase.END) {
					GeckoAnim${customBlockIndex}.this.ticks += 1;
					if (GeckoAnim${customBlockIndex}.this.ticks >= GeckoAnim${customBlockIndex}.this.waitTicks)
						run();
				}
			}

			private void run() {
				MinecraftForge.EVENT_BUS.unregister(GeckoAnim${customBlockIndex}.this);
				${input$entity}.getPersistentData().putString("animation", ${input$animation});
			}
		}
		new GeckoAnim${customBlockIndex}().start(world, 2);
