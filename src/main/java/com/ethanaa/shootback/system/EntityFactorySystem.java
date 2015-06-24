package com.ethanaa.shootback.system;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Point3D;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.GroupManager;
import com.artemis.managers.PlayerManager;
import com.artemis.managers.TagManager;
import com.artemis.systems.VoidEntitySystem;
import com.artemis.utils.EntityBuilder;
import com.ethanaa.shootback.component.EntityMesh;
import com.ethanaa.shootback.component.Health;
import com.ethanaa.shootback.component.Position;
import com.ethanaa.shootback.manager.GuiManager;
import com.ethanaa.shootback.type.EntityType;
import com.ethanaa.shootback.type.Tag;
import com.ethanaa.shootback.util.ResourceUtil;

@Wire
public class EntityFactorySystem extends VoidEntitySystem {

	private final Logger logger = LoggerFactory.getLogger(EntityFactorySystem.class);
	
	private TagManager tagManager;
	private PlayerManager playerManager;
	private GroupManager groupManager;
	private GuiManager guiManager;
	
	private final Map<EntityType, Archetype> archetypes = new HashMap<>();
	
	@Override
	protected void processSystem() {		
	}
	
	public void initGame() {
		
		createArchetypes();		
		createShip(Tag.PLAYER_SHIP, 0d, 0d, 0d);		
		createShip(Tag.ENEMY_SHIP, 2d, 2d, 0d);		
	}
	
	public void createArchetypes() {
		
		archetypes.put(EntityType.SHIP, new ArchetypeBuilder()
			.add(Health.class)
			.add(Position.class)
			.add(EntityMesh.class)
			.build(world));				
	}

	public Entity createShip(Tag tag, double x, double y, double z) {		
		
		PhongMaterial material = new PhongMaterial();
		material.setDiffuseMap(ResourceUtil.getImage("icosah_net.gif"));
		
		MeshView meshView = new MeshView(createShipMesh());
		meshView.setCullFace(CullFace.FRONT);
		meshView.setMaterial(material);
		
		Entity ship = new EntityBuilder(world).with(
				new Health(500), 
				new Position(new Point3D(x, y, z), new Rotate(0d, Rotate.Y_AXIS)), 
				new EntityMesh(meshView))
				.build();
		
		tagManager.register(tag.name(), ship);
		
		return ship;
	}
	
	private Mesh createShipMesh() {
		
	    TriangleMesh m = new TriangleMesh();

	    // POINTS
	    m.getPoints().addAll(
	        0f, 0f, -0.951057f, 
	        0f, 0f, 0.951057f, 
	        -0.850651f, 0f, -0.425325f, 
	        0.850651f, 0f, 0.425325f, 
	        0.688191f, -0.5f, -0.425325f, 
	        0.688191f, 0.5f, -0.425325f, 
	        -0.688191f, -0.5f, 0.425325f, 
	        -0.688191f, 0.5f, 0.425325f, 
	        -0.262866f, -0.809017f, -0.425325f, 
	        -0.262866f, 0.809017f, -0.425325f, 
	        0.262866f, -0.809017f, 0.425325f, 
	        0.262866f, 0.809017f, 0.425325f
	    );

	    // TEXTURES
	    m.getTexCoords().addAll(
	            0.181818f, 0f, 
	            0.363636f, 0f, 
	            0.545455f, 0f, 
	            0.727273f, 0f, 
	            0.909091f, 0f,
	            0.0909091f, 0.333333f,
	            0.272727f, 0.333333f, 
	            0.454545f, 0.333333f, 
	            0.636364f, 0.333333f, 
	            0.818182f, 0.333333f, 
	            1f, 0.333333f, 
	            0f, 0.666667f, 
	            0.181818f, 0.666667f, 
	            0.363636f, 0.666667f, 
	            0.545455f, 0.666667f, 
	            0.727273f, 0.666667f, 
	            0.909091f, 0.666667f, 
	            0.0909091f, 1f, 
	            0.272727f, 1f, 
	            0.454545f, 1f, 
	            0.636364f, 1f, 
	            0.818182f, 1f
	    );

	    // FACES
	    m.getFaces().addAll(
	            1, 6, 11, 5, 7, 0, 
	            1, 12, 7, 11, 6, 5, 
	            1, 7, 6, 6, 10, 1, 
	            1, 13, 10, 12, 3, 6, 
	            1, 8, 3, 7, 11, 2,
	            4, 14, 8, 13, 0, 7, 
	            5, 9, 4, 8, 0, 3, 
	            9, 15, 5, 14, 0, 8, 
	            2, 10, 9, 9, 0, 4, 
	            8, 16, 2, 15, 0, 9,
	            11, 5, 9, 6, 7, 12,
	            7, 11, 2, 12, 6, 17, 
	            6, 6, 8, 7, 10, 13, 
	            10, 12, 4, 13, 3, 18, 
	            3, 7, 5, 8, 11, 14,
	            4, 13, 10, 14, 8, 19, 
	            5, 8, 3, 9, 4, 15, 
	            9, 14, 11, 15, 5, 20, 
	            2, 9, 7, 10, 9, 16, 
	            8, 15, 6, 16, 2, 21
	    );	    
	    
	    return m;		
	}
}
