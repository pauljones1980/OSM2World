package org.osm2world.core.world.modules;

import java.util.HashMap;
import java.util.Map;

import org.openstreetmap.josm.plugins.graphview.core.data.Tag;
import org.openstreetmap.josm.plugins.graphview.core.data.TagGroup;
import org.osm2world.core.map_data.data.MapArea;
import org.osm2world.core.map_elevation.data.GroundState;
import org.osm2world.core.math.VectorXZ;
import org.osm2world.core.target.Material;
import org.osm2world.core.target.RenderableToAllTargets;
import org.osm2world.core.target.Target;
import org.osm2world.core.world.data.AbstractAreaWorldObject;
import org.osm2world.core.world.data.TerrainBoundaryWorldObject;
import org.osm2world.core.world.modules.common.AbstractModule;
import org.osm2world.core.world.modules.common.Materials;

/**
 * adds generic areas with surface information to the world.
 * Is based on surface information on otherwise unknown/unspecified areas.
 */
public class SurfaceAreaModule extends AbstractModule {

	private static final Map<Tag, String> defaultSurfaceMap
		= new HashMap<Tag, String>();
	
	static {
		defaultSurfaceMap.put(new Tag("leisure", "pitch"), "ground");
	}
	
	@Override
	protected void applyToArea(MapArea area) {
		
		if (!area.getRepresentations().isEmpty()) return;
		
		TagGroup tags = area.getTags();
		
		if (tags.containsKey("surface")) {
			area.addRepresentation(new SurfaceArea(area, tags.getValue("surface")));
		} else {
		
			for (Tag tagWithDefault : defaultSurfaceMap.keySet()) {
				if (tags.contains(tagWithDefault)) {
					area.addRepresentation(new SurfaceArea(
							area, defaultSurfaceMap.get(tagWithDefault)));
				}
			}
			
		}
		
	}
	
	private class SurfaceArea extends AbstractAreaWorldObject
		implements RenderableToAllTargets, TerrainBoundaryWorldObject {
		
		private final String surface;
		
		public SurfaceArea(MapArea area, String surface) {
			super(area);
			this.surface = surface;
		}

		@Override
		public void renderTo(Target target) {
			
			Material material = Materials.getMaterial(surface);
			
			if (material != null) {
				target.drawTriangles(material, getTriangulation());
			}
			
		}
		
		@Override
		public GroundState getGroundState() {
			if (BridgeModule.isBridge(area.getTags())) {
				return GroundState.ABOVE;
			} else if (TunnelModule.isTunnel(area.getTags())) {
				return GroundState.BELOW;
			} else {
				return GroundState.ON;
			}
		}
		
		@Override
		public double getClearingAbove(VectorXZ pos) {
			return 0;
		}
		
		@Override
		public double getClearingBelow(VectorXZ pos) {
			return 0;
		}
		
	}

}