package org.osm2world.viewer.view.debug;

import java.awt.Color;
import java.util.List;

import javax.media.opengl.GL;

import org.osm2world.core.math.VectorXYZ;
import org.osm2world.core.target.common.rendering.Camera;
import org.osm2world.core.target.common.rendering.OrthoTilesUtil;
import org.osm2world.core.target.jogl.JOGLTarget;
import org.osm2world.core.target.jogl.RenderableToJOGL;
import org.osm2world.viewer.control.actions.OrthoBoundsAction;

/**
 * illustrates the construction of the orthogonal perspective
 * as set by {@link OrthoBoundsAction}
 */
public class OrthoBoundsDebugView extends DebugView
		implements RenderableToJOGL {
	
	@Override
	public String getDescription() {
		return "illustrates the construction of the orthogonal perspective";
	}
	
	private static final Color LINE_COLOR = Color.YELLOW;
	private static final Color POINT_COLOR = Color.RED;
	
	private static final float HALF_POINT_WIDTH = 0.4f;
	
	@Override
	public void renderToImpl(GL gl, Camera camera) {
		
		if (map == null) { return; }
		
		JOGLTarget target = new JOGLTarget(gl, camera);
		
		Camera orthoCam = OrthoTilesUtil.cameraForBounds(map.getBoundary(), 30);
		
		List<VectorXYZ> boundVertices = map.getBoundary().polygonXZ().xyz(0).getVertices();
		target.drawLineLoop(LINE_COLOR, boundVertices);
		target.drawLineStrip(LINE_COLOR, boundVertices.get(0), boundVertices.get(2));
		target.drawLineStrip(LINE_COLOR, boundVertices.get(1), boundVertices.get(3));
		
		drawBoxAround(target, orthoCam.getPos(), POINT_COLOR, HALF_POINT_WIDTH);
		drawBoxAround(target, orthoCam.getLookAt(), POINT_COLOR, HALF_POINT_WIDTH);
		
		target.drawLineStrip(LINE_COLOR, orthoCam.getPos(), orthoCam.getLookAt());
		
	}
	
}