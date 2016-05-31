

/**
 * Describe class (Step 5) Example3D here.
 *
 * Time-stamp: <2016-02-17 16:46:31 rlc3> edited by rlc 
 *
 */

import java.awt.BorderLayout;
import java.awt.Container;

import javax.media.j3d.Alpha;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Material;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JApplet;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class Example3D extends JApplet {

	/*
	 * This class will set up the environment to quickly get the Java program
	 * running. It creates all of the necessary objects on the view side of the
	 * scene graph. The SimpleUniverse class extends the VirtualUniverse class
	 * which creates a locale - a single ViewingPlatform and a single Viewer
	 * object. This class provides my program with all the functionality needed.
	 * If there were more advanced programs requiring more functionality, they
	 * would have to use something other than this class to get more
	 * functionality.
	 */
	private SimpleUniverse universe = null;
	Canvas3D c;

	// create a "standard" universe using SimpleUniverse
	public Example3D() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		// Here we create a canvas, but if we didn't the universe
		// would create one by default
		c = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
		cp.add("Center", c);
		universe = new SimpleUniverse(c);
		BranchGroup scene = createSceneGraph();
		universe.getViewingPlatform().setNominalViewingTransform();
		universe.addBranchGraph(scene);

		// create a viewing platform
		TransformGroup cameraTG = universe.getViewingPlatform()
				.getViewPlatformTransform();
		// starting position of the viewing platform
		Vector3f translate = new Vector3f();
		Transform3D T3D = new Transform3D();
		// move along z axis by (x, y, 15.0f)
		// ("move away from the screen")
		translate.set(0.0f, 0.0f, 15.0f);
		T3D.setTranslation(translate);
		cameraTG.setTransform(T3D);

	}

	public BranchGroup createSceneGraph() {

		// creating the spherical bounding region defined by a centre point
		// and radius.- See mouse behaviour below
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
				1000.0);

		// creating a branch group for the root of our graph
		BranchGroup objRoot = new BranchGroup();

		// creating a transform group to hold all the object in our solar system
		// This will not be used for planets that do not have the sun as their
		// centre point - such as the moon belonging to the Earth.
		TransformGroup mainTG = new TransformGroup();
		mainTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		mainTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		// Here I initiate branch objects to organise the planet and translation
		// objects in a structured manner - to be grouped individually.
		BranchGroup sunBranch = new BranchGroup();
		BranchGroup mercuryBranch = new BranchGroup();
		BranchGroup venusBranch = new BranchGroup();
		BranchGroup earthBranch = new BranchGroup();
		BranchGroup moonBranch = new BranchGroup();
		BranchGroup marsBranch = new BranchGroup();
		BranchGroup jupiterBranch = new BranchGroup();
		BranchGroup saturnBranch = new BranchGroup();
		BranchGroup uranusBranch = new BranchGroup();
		BranchGroup neptuneBranch = new BranchGroup();
		BranchGroup plutoBranch = new BranchGroup();

		// Sun-----------------------------------------------------------
		Transform3D sunTF = new Transform3D();
		TransformGroup sunTFGroup = new TransformGroup(sunTF);
		sunTF.setScale(new Vector3d(1.0, 1.0, 1.0));

		// Set up colors

		Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
		Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
		Color3f red = new Color3f(0.7f, .15f, .15f);

		// SUN'S Texture loader
		TextureLoader sunTextureLoad = new TextureLoader("sunTexture.jpg",
				new Container());

		Texture sunTexture = sunTextureLoad.getTexture();

		// S and T are like X and Y coordinates on the texture
		sunTexture.setBoundaryModeS(Texture.WRAP);
		sunTexture.setBoundaryModeT(Texture.WRAP);
		sunTexture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));

		// Set up the texture attributes

		// could be REPLACE, BLEND or DECAL instead of MODULATE

		TextureAttributes texAttr = new TextureAttributes();
		texAttr.setTextureMode(TextureAttributes.MODULATE);

		Appearance ap = new Appearance();
		ap.setTexture(sunTexture);
		ap.setTextureAttributes(texAttr);

		// set up the material
		ap.setMaterial(new Material(red, black, red, black, 1.0f));

		TextureAttributes sunTextureAtttributes = new TextureAttributes();
		sunTextureAtttributes.setTextureMode(TextureAttributes.MODULATE);

		Appearance sunsAppearance = new Appearance();
		sunsAppearance.setTexture(sunTexture);
		sunsAppearance.setTextureAttributes(sunTextureAtttributes);

		// Creating the shape/planet
		// When using texture on primitive shapes, must use primitive flags
		// to generate normals and texture coordinates
		int sunPrimFlag = Primitive.GENERATE_NORMALS
				+ Primitive.GENERATE_TEXTURE_COORDS;

		Sphere sunSphere = new Sphere(0.5f, sunPrimFlag, 1000, sunsAppearance);

		// Adding lighting to the sun
		Color3f lightOnSun = new Color3f(1.0f, 0.0f, 0.0f);
		Vector3f lightOnSunDirection = new Vector3f(0f, 1f, 0.2f);
		DirectionalLight light1OnSun = new DirectionalLight(lightOnSun,
				lightOnSunDirection);
		light1OnSun.setInfluencingBounds(bounds);

		AmbientLight ambientLight = new AmbientLight(new Color3f(.5f, .5f, .5f));
		ambientLight.setInfluencingBounds(bounds);

		// Mercury-------------------------------------------------------
		Transform3D mercuryTransform = new Transform3D();
		TransformGroup mercuryTG = new TransformGroup();
		mercuryTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		// orbit on mercury planet
		Alpha mercuryOrbit = new Alpha(-1, 47400);

		RotationInterpolator mercurySpin = new RotationInterpolator(
				mercuryOrbit, mercuryTG, mercuryTransform, 0.0f,
				(float) Math.PI * 2);
		mercurySpin.setSchedulingBounds(bounds);

		// Setting scales
		Transform3D mercuryTransform1 = new Transform3D();
		mercuryTransform1.setScale(new Vector3d(0.15, 0.15, 0.15));
		mercuryTransform1.setTranslation(new Vector3d(0.0, 0.0, -1));
		TransformGroup mercuryTG1 = new TransformGroup(mercuryTransform1);

		TransformGroup mercuryTG2 = new TransformGroup();
		mercuryTG2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		Transform3D mercuryTransform2 = new Transform3D();

		// Transformation spin for mercury
		Alpha mercuryOrbit1 = new Alpha(-1, 19000);
		RotationInterpolator mercurySpin1 = new RotationInterpolator(
				mercuryOrbit1, mercuryTG2, mercuryTransform2, 0.0f,
				(float) Math.PI * 2);
		mercurySpin1.setSchedulingBounds(bounds);

		// Texture for mercury planet
		TextureLoader mercuryTextureLoad = new TextureLoader("MercTexture.jpg",
				new Container());

		Texture mercuryTexture = mercuryTextureLoad.getTexture();
		mercuryTexture.setBoundaryModeS(Texture.WRAP);
		mercuryTexture.setBoundaryModeT(Texture.WRAP);
		mercuryTexture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
		TextureAttributes mercuryTextureAtttributes = new TextureAttributes();

		Appearance mercuryAppearance = new Appearance();
		mercuryAppearance.setTexture(mercuryTexture);
		mercuryAppearance.setTextureAttributes(mercuryTextureAtttributes);
		int mercPrim = Primitive.GENERATE_NORMALS
				+ Primitive.GENERATE_TEXTURE_COORDS;

		Sphere mercSphere = new Sphere(0.5f, mercPrim, 1000, mercuryAppearance);

		// Venus---------------------------------------------------------
		TransformGroup venusTG = new TransformGroup();
		venusTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		// Venus orbit speed
		Alpha venusOrbit = new Alpha(-1, 35000);

		Transform3D venusTransform = new Transform3D();

		// this will set the speed of the object
		RotationInterpolator venusSpin = new RotationInterpolator(venusOrbit,
				venusTG, venusTransform, 0.0f, (float) Math.PI * 2);
		venusSpin.setSchedulingBounds(bounds);

		// Setting the size of the planet using setScale
		Transform3D venusPlanet = new Transform3D();
		venusPlanet.setScale(new Vector3d(0.2, 0.2, 0.2));
		venusPlanet.setTranslation(new Vector3d(0.0, 0.0, -1.5));

		TransformGroup venusTG1 = new TransformGroup(venusPlanet);

		// Transformation spin for venus
		TransformGroup venusTG2 = new TransformGroup();
		venusTG2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D venusTransform1 = new Transform3D();

		Alpha venusOrbit1 = new Alpha(-1, 19000);

		RotationInterpolator venusSpin1 = new RotationInterpolator(venusOrbit1,
				venusTG2, venusTransform1, 0.0f, (float) Math.PI * 2);
		venusSpin1.setSchedulingBounds(bounds);

		// Texture for venus planet
		TextureLoader venusTextureLoad = new TextureLoader("venusTexture.jpg",
				new Container());

		Texture venusTexture = venusTextureLoad.getTexture();
		venusTexture.setBoundaryModeS(Texture.WRAP);
		venusTexture.setBoundaryModeT(Texture.WRAP);
		venusTexture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));

		TextureAttributes venusTextureAtttributes = new TextureAttributes();

		Appearance venusAppearance = new Appearance();
		venusAppearance.setTexture(venusTexture);
		venusAppearance.setTextureAttributes(venusTextureAtttributes);

		int venusPrim = Primitive.GENERATE_NORMALS
				+ Primitive.GENERATE_TEXTURE_COORDS;

		Sphere venusSphere = new Sphere(0.5f, venusPrim, 1000, venusAppearance);

		// Earth----------------------------------------------------------
		TransformGroup earthTG = new TransformGroup();
		earthTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		// Earth orbit speed
		Alpha earthOrbit = new Alpha(-1, 29800);

		Transform3D earthTransform = new Transform3D();

		// this will set the speed of the object
		RotationInterpolator earthSpin = new RotationInterpolator(earthOrbit,
				earthTG, earthTransform, 0.0f, (float) Math.PI * 2);
		earthSpin.setSchedulingBounds(bounds);

		// Setting the size of the planet using setScale
		Transform3D earthPlanet = new Transform3D();
		earthPlanet.setScale(new Vector3d(0.2, 0.2, 0.2));
		earthPlanet.setTranslation(new Vector3d(0.0, 0.0, -2.0));

		TransformGroup earthTG1 = new TransformGroup(earthPlanet);

		// Transformation spin for earth
		TransformGroup earthTG2 = new TransformGroup();
		earthTG2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D earthTransform1 = new Transform3D();
		Alpha earthOrbit1 = new Alpha(-1, 19000);

		RotationInterpolator earthSpin1 = new RotationInterpolator(earthOrbit1,
				earthTG2, earthTransform1, 0.0f, (float) Math.PI * 2);

		earthSpin1.setSchedulingBounds(bounds);

		// Texture for earth planet
		TextureLoader earthTextureLoad = new TextureLoader("earthTexture.jpg",
				new Container());

		Texture earthTexture = earthTextureLoad.getTexture();
		earthTexture.setBoundaryModeS(Texture.WRAP);
		earthTexture.setBoundaryModeT(Texture.WRAP);
		earthTexture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));

		TextureAttributes earthTextureAtttributes = new TextureAttributes();

		Appearance earthAppearance = new Appearance();
		earthAppearance.setTexture(earthTexture);
		earthAppearance.setTextureAttributes(earthTextureAtttributes);

		int earthPrim = Primitive.GENERATE_NORMALS
				+ Primitive.GENERATE_TEXTURE_COORDS;

		Sphere earthSphere = new Sphere(0.5f, earthPrim, 1000, earthAppearance);

		// Moon ---------------------------------------------------------
		// This moon will orbit the Earth and spin on it's own x axis
		TransformGroup moonTG = new TransformGroup();
		moonTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		// Moon orbit around earth
		Alpha moonOrbit = new Alpha(-1, 10000);
		Transform3D moonTransform = new Transform3D();

		// this will set the speed of the object
		RotationInterpolator moonSpin = new RotationInterpolator(moonOrbit,
				moonTG, moonTransform, 0.0f, (float) Math.PI * 2);

		moonSpin.setSchedulingBounds(bounds);

		// Setting the size of the planet using setScale
		Transform3D moonPlanet = new Transform3D();

		moonPlanet.setScale(new Vector3d(0.05, 0.05, 0.05));
		moonPlanet.setTranslation(new Vector3d(0.0, 0.0, -0.75));

		TransformGroup moonTG1 = new TransformGroup(moonPlanet);

		// Transformation spin for mars
		TransformGroup moonTG2 = new TransformGroup();
		moonTG2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D moonTransform1 = new Transform3D();
		Alpha moonOrbit1 = new Alpha(-1, 19000);

		RotationInterpolator moonSpin1 = new RotationInterpolator(moonOrbit1,
				moonTG2, moonTransform1, 0.0f, (float) Math.PI * 2);

		moonSpin1.setSchedulingBounds(bounds);

		// Texture for mars planet
		TextureLoader moonTextureLoad = new TextureLoader("moonTexture.jpg",
				new Container());

		Texture moonTexture = moonTextureLoad.getTexture();
		moonTexture.setBoundaryModeS(Texture.WRAP);
		moonTexture.setBoundaryModeT(Texture.WRAP);
		moonTexture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));

		TextureAttributes moonTextureAtttributes = new TextureAttributes();

		Appearance moonAppearance = new Appearance();
		moonAppearance.setTexture(moonTexture);
		moonAppearance.setTextureAttributes(moonTextureAtttributes);
		int moonPrim = Primitive.GENERATE_NORMALS
				+ Primitive.GENERATE_TEXTURE_COORDS;

		Sphere moonSphere = new Sphere(0.5f, moonPrim, 1000, moonAppearance);

		// Mars----------------------------------------------------------
		TransformGroup marsTG = new TransformGroup();
		marsTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		// mars orbit
		Alpha marsOrbit = new Alpha(-1, 24100);
		Transform3D marsTransform = new Transform3D();

		// this will set the speed of the object
		RotationInterpolator marsSpin = new RotationInterpolator(marsOrbit,
				marsTG, marsTransform, 0.0f, (float) Math.PI * 2);

		marsSpin.setSchedulingBounds(bounds);

		// Setting the size of the planet using setScale
		Transform3D marsPlanet = new Transform3D();

		marsPlanet.setScale(new Vector3d(0.125, 0.125, 0.125));
		marsPlanet.setTranslation(new Vector3d(0.0, 0.0, -2.5));

		TransformGroup marsTG1 = new TransformGroup(marsPlanet);

		// Transformation spin for mars
		TransformGroup marsTG2 = new TransformGroup();
		marsTG2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D marsTransform1 = new Transform3D();
		Alpha marsOrbit1 = new Alpha(-1, 19000);

		RotationInterpolator marsSpin1 = new RotationInterpolator(marsOrbit1,
				marsTG2, marsTransform1, 0.0f, (float) Math.PI * 2);

		marsSpin1.setSchedulingBounds(bounds);

		// Texture for mars planet
		TextureLoader marsTextureLoad = new TextureLoader("marsTexture.jpg",
				new Container());

		Texture marsTexture = marsTextureLoad.getTexture();
		marsTexture.setBoundaryModeS(Texture.WRAP);
		marsTexture.setBoundaryModeT(Texture.WRAP);
		marsTexture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));

		TextureAttributes marsTextureAtttributes = new TextureAttributes();

		Appearance marsAppearance = new Appearance();
		marsAppearance.setTexture(marsTexture);
		marsAppearance.setTextureAttributes(marsTextureAtttributes);
		int marsPrim = Primitive.GENERATE_NORMALS
				+ Primitive.GENERATE_TEXTURE_COORDS;

		Sphere marsSphere = new Sphere(0.5f, marsPrim, 1000, marsAppearance);

		// Jupiter----------------------------------------------------------
		TransformGroup jupiterTG = new TransformGroup();
		jupiterTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		// Jupiters orbit
		Alpha jupiterOrbit = new Alpha(-1, 13100);
		Transform3D jupiterTransform = new Transform3D();

		// this will set the speed of the object
		RotationInterpolator jupiterSpin = new RotationInterpolator(
				jupiterOrbit, jupiterTG, jupiterTransform, 0.0f,
				(float) Math.PI * 2);
		jupiterSpin.setSchedulingBounds(bounds);

		// Setting the size of the planet using setScale
		Transform3D jupiterPlanet = new Transform3D();
		jupiterPlanet.setScale(new Vector3d(0.5, 0.5, 0.5));
		jupiterPlanet.setTranslation(new Vector3d(0.0, 0.0, -3.0));
		TransformGroup jupiterTG1 = new TransformGroup(jupiterPlanet);

		// Transformation spin for jupiter
		TransformGroup jupiterTG2 = new TransformGroup();
		jupiterTG2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D jupiterTransform1 = new Transform3D();
		Alpha jupiterOrbit1 = new Alpha(-1, 19000);

		RotationInterpolator jupiterSpin1 = new RotationInterpolator(
				jupiterOrbit1, jupiterTG2, jupiterTransform1, 0.0f,
				(float) Math.PI * 2);
		jupiterSpin1.setSchedulingBounds(bounds);

		// Texture for jupiter planet
		TextureLoader jupiterTextureLoad = new TextureLoader(
				"jupiterTexture.jpg", new Container());

		Texture jupiterTexture = jupiterTextureLoad.getTexture();
		jupiterTexture.setBoundaryModeS(Texture.WRAP);
		jupiterTexture.setBoundaryModeT(Texture.WRAP);
		jupiterTexture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
		TextureAttributes jupiterTextureAtttributes = new TextureAttributes();

		Appearance jupiterAppearance = new Appearance();
		jupiterAppearance.setTexture(jupiterTexture);
		jupiterAppearance.setTextureAttributes(jupiterTextureAtttributes);
		int jupiterPrim = Primitive.GENERATE_NORMALS
				+ Primitive.GENERATE_TEXTURE_COORDS;

		Sphere jupiterSphere = new Sphere(0.5f, jupiterPrim, 1000,
				jupiterAppearance);

		// Saturn----------------------------------------------------------
		TransformGroup saturnTG = new TransformGroup();
		saturnTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		// Saturn orbit
		Alpha saturnOrbit = new Alpha(-1, 97000);
		Transform3D saturnTransform = new Transform3D();

		// this will set the speed of the object
		RotationInterpolator saturnSpin = new RotationInterpolator(saturnOrbit,
				saturnTG, saturnTransform, 0.0f, (float) Math.PI * 2);
		saturnSpin.setSchedulingBounds(bounds);

		// Setting the size of the planet using setScale
		Transform3D saturnPlanet = new Transform3D();
		saturnPlanet.setScale(new Vector3d(0.4, 0.4, 0.4));
		saturnPlanet.setTranslation(new Vector3d(0.0, 0.0, -3.75));
		TransformGroup saturnTG1 = new TransformGroup(saturnPlanet);

		// Transformation spin for Saturn
		TransformGroup saturnTG2 = new TransformGroup();
		saturnTG2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		Transform3D saturnTransform1 = new Transform3D();
		Alpha saturnOrbit1 = new Alpha(-1, 19000);
		RotationInterpolator saturnSpin1 = new RotationInterpolator(
				saturnOrbit1, saturnTG2, saturnTransform1, 0.0f,
				(float) Math.PI * 2);
		saturnSpin1.setSchedulingBounds(bounds);

		// Texture for saturn planet
		TextureLoader saturnTextureLoad = new TextureLoader(
				"saturnTexture.jpg", new Container());

		Texture saturnTexture = saturnTextureLoad.getTexture();
		saturnTexture.setBoundaryModeS(Texture.WRAP);
		saturnTexture.setBoundaryModeT(Texture.WRAP);
		saturnTexture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
		TextureAttributes saturnTextureAtttributes = new TextureAttributes();

		Appearance saturnAppearance = new Appearance();
		saturnAppearance.setTexture(saturnTexture);
		saturnAppearance.setTextureAttributes(saturnTextureAtttributes);
		int saturnPrim = Primitive.GENERATE_NORMALS
				+ Primitive.GENERATE_TEXTURE_COORDS;

		Sphere saturnSphere = new Sphere(0.5f, saturnPrim, 1000,
				saturnAppearance);

		// Texture for the ring around saturn
		TextureLoader saturnRingTextureLoad = new TextureLoader(
				"plutoTexture.jpg", new Container());

		Texture saturnRingTexture = saturnRingTextureLoad.getTexture();
		saturnRingTexture.setBoundaryModeS(Texture.WRAP);
		saturnRingTexture.setBoundaryModeT(Texture.WRAP);
		saturnRingTexture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
		TextureAttributes saturnRingTextureAtttributes = new TextureAttributes();

		Appearance saturnRingAppearance = new Appearance();
		saturnRingAppearance.setTexture(saturnRingTexture);
		saturnRingAppearance.setTextureAttributes(saturnRingTextureAtttributes);

		// Texture for making the ring around saturn appear hollow
		TextureLoader saturnHollowRingTextureLoad = new TextureLoader(
				"black.jpg", new Container());

		Texture saturnHollowRingTexture = saturnHollowRingTextureLoad
				.getTexture();
		saturnHollowRingTexture.setBoundaryModeS(Texture.WRAP);
		saturnHollowRingTexture.setBoundaryModeT(Texture.WRAP);
		saturnHollowRingTexture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f,
				0.0f));
		TextureAttributes saturnHollowRingTextureAtttributes = new TextureAttributes();

		Appearance saturnHollowRingAppearance = new Appearance();
		saturnHollowRingAppearance.setTexture(saturnHollowRingTexture);
		saturnHollowRingAppearance
				.setTextureAttributes(saturnHollowRingTextureAtttributes);

		Transform3D saturnDustTF = new Transform3D();
		TransformGroup saturnDustTG = new TransformGroup(saturnDustTF);
		Cylinder saturnRing = new Cylinder(0.9f, 0.01f, saturnPrim,
				saturnRingAppearance);

		Cylinder makeRingHollow = new Cylinder(0.6f, 0.01f, saturnPrim,
				saturnHollowRingAppearance);

		// Uranus----------------------------------------------------------
		TransformGroup uranusTG = new TransformGroup();
		uranusTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		// Uranus orbit
		Alpha uranusOrbit = new Alpha(-1, 68000);
		Transform3D uranusTransform = new Transform3D();

		// this will set the speed of the object
		RotationInterpolator uranusSpin = new RotationInterpolator(uranusOrbit,
				uranusTG, uranusTransform, 0.0f, (float) Math.PI * 2);
		uranusSpin.setSchedulingBounds(bounds);

		// Setting the size of the planet using setScale
		Transform3D uranusPlanet = new Transform3D();
		uranusPlanet.setScale(new Vector3d(0.3, 0.3, 0.3));
		uranusPlanet.setTranslation(new Vector3d(0.0, 0.0, -4.35));
		TransformGroup uranusTG1 = new TransformGroup(uranusPlanet);

		// Transformation spin for uranus
		TransformGroup uranusTG2 = new TransformGroup();
		uranusTG2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		Transform3D uranusTransform1 = new Transform3D();
		Alpha uranusOrbit1 = new Alpha(-1, 19000);
		RotationInterpolator uranusSpin1 = new RotationInterpolator(
				uranusOrbit1, uranusTG2, uranusTransform1, 0.0f,
				(float) Math.PI * 2);
		uranusSpin1.setSchedulingBounds(bounds);

		// Texture for uranus planet
		TextureLoader uranusTextureLoad = new TextureLoader(
				"uranusTexture.jpg", new Container());

		Texture uranusTexture = uranusTextureLoad.getTexture();
		uranusTexture.setBoundaryModeS(Texture.WRAP);
		uranusTexture.setBoundaryModeT(Texture.WRAP);
		uranusTexture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
		TextureAttributes uranusTextureAtttributes = new TextureAttributes();

		Appearance uranusAppearance = new Appearance();
		uranusAppearance.setTexture(uranusTexture);
		uranusAppearance.setTextureAttributes(uranusTextureAtttributes);
		int uranusPrim = Primitive.GENERATE_NORMALS
				+ Primitive.GENERATE_TEXTURE_COORDS;

		Sphere uranusSphere = new Sphere(0.5f, uranusPrim, 1000,
				uranusAppearance);

		// NEPTUNE----------------------------------------------------------
		TransformGroup neptuneTG = new TransformGroup();
		neptuneTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		// Neptune orbit
		Alpha neptuneOrbit = new Alpha(-1, 54000);
		Transform3D neptuneTransform = new Transform3D();

		// this will set the speed of the object
		RotationInterpolator neptuneSpin = new RotationInterpolator(
				neptuneOrbit, neptuneTG, neptuneTransform, 0.0f,
				(float) Math.PI * 2);
		neptuneSpin.setSchedulingBounds(bounds);

		// Setting the size of the planet using setScale
		Transform3D neptunePlanet = new Transform3D();
		neptunePlanet.setScale(new Vector3d(0.3, 0.3, 0.3));
		neptunePlanet.setTranslation(new Vector3d(0.0, 0.0, -5.05));
		TransformGroup neptuneTG1 = new TransformGroup(neptunePlanet);

		// Transformation spin for neptune
		TransformGroup neptuneTG2 = new TransformGroup();
		neptuneTG2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		Transform3D neptuneTransform1 = new Transform3D();
		Alpha neptuneOrbit1 = new Alpha(-1, 19000);
		RotationInterpolator neptuneSpin1 = new RotationInterpolator(
				neptuneOrbit1, neptuneTG2, neptuneTransform1, 0.0f,
				(float) Math.PI * 2);
		neptuneSpin1.setSchedulingBounds(bounds);

		// Texture for neptune planet
		TextureLoader neptuneTextureLoad = new TextureLoader(
				"neptuneTexture.jpg", new Container());

		Texture neptuneTexture = neptuneTextureLoad.getTexture();
		neptuneTexture.setBoundaryModeS(Texture.WRAP);
		neptuneTexture.setBoundaryModeT(Texture.WRAP);
		neptuneTexture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
		TextureAttributes neptuneTextureAtttributes = new TextureAttributes();

		Appearance neptuneAppearance = new Appearance();
		neptuneAppearance.setTexture(neptuneTexture);
		neptuneAppearance.setTextureAttributes(neptuneTextureAtttributes);
		int neptunePrim = Primitive.GENERATE_NORMALS
				+ Primitive.GENERATE_TEXTURE_COORDS;

		Sphere neptuneSphere = new Sphere(0.5f, neptunePrim, 1000,
				neptuneAppearance);

		// PLUTO--------------------------------------------------------
		TransformGroup plutoTG = new TransformGroup();
		plutoTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		// Pluto orbit
		Alpha plutoOrbit = new Alpha(-1, 47000);
		Transform3D plutoTransform = new Transform3D();

		// this will set the speed of the object
		RotationInterpolator plutoSpin = new RotationInterpolator(plutoOrbit,
				plutoTG, plutoTransform, 0.0f, (float) Math.PI * 2);
		plutoSpin.setSchedulingBounds(bounds);

		// Setting the size of the planet using setScale
		Transform3D plutoPlanet = new Transform3D();
		plutoPlanet.setScale(new Vector3d(0.1, 0.1, 0.1));
		plutoPlanet.setTranslation(new Vector3d(0.0, 0.0, -5.8));
		TransformGroup plutoTG1 = new TransformGroup(plutoPlanet);

		// Transformation spin for pluto
		TransformGroup plutoTG2 = new TransformGroup();
		plutoTG2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		Transform3D plutoTransform1 = new Transform3D();
		Alpha plutoOrbit1 = new Alpha(-1, 19000);
		RotationInterpolator plutoSpin1 = new RotationInterpolator(plutoOrbit1,
				plutoTG2, plutoTransform1, 0.0f, (float) Math.PI * 2);
		plutoSpin1.setSchedulingBounds(bounds);

		// Texture for pluto planet
		TextureLoader plutoTextureLoad = new TextureLoader("plutoTexture.jpg",
				new Container());

		Texture plutoTexture = plutoTextureLoad.getTexture();
		plutoTexture.setBoundaryModeS(Texture.WRAP);
		plutoTexture.setBoundaryModeT(Texture.WRAP);
		plutoTexture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
		TextureAttributes plutoTextureAtttributes = new TextureAttributes();

		Appearance plutoAppearance = new Appearance();
		plutoAppearance.setTexture(plutoTexture);
		plutoAppearance.setTextureAttributes(plutoTextureAtttributes);
		int plutoPrim = Primitive.GENERATE_NORMALS
				+ Primitive.GENERATE_TEXTURE_COORDS;

		Sphere plutoSphere = new Sphere(0.5f, plutoPrim, 1000, plutoAppearance);

		// ROCKET CREATION------------------------------------------
		// Here I will create a rocket using my own shapes which includes
		// a cylinder for the main body of the rocket, a cone for the pointy
		// part of the rocket, and finally, 2 crossed flat square-shaped
		// cuboids which form the rocket blades.

		// creating blue colour
		Appearance blueColour = new Appearance();
		Color3f blueColor = new Color3f(0.0f, 0.0f, 0.6f);
		ColoringAttributes blueCA = new ColoringAttributes();
		blueCA.setColor(blueColor);
		blueColour.setColoringAttributes(blueCA);

		// Rocket body cylinder
		Transform3D rocketTransform = new Transform3D();
		rocketTransform.setTranslation(new Vector3d(0, 0, -2));
		TransformGroup rocketAssembly = new TransformGroup(rocketTransform);
		Cylinder rocketCylinder = new Cylinder(0.1f, 0.5f);
		rocketAssembly.addChild(rocketCylinder);

		// Rocket cone
		Transform3D rocketConeTransform = new Transform3D();
		rocketConeTransform.setTranslation(new Vector3d(0, 0.35, 0));
		TransformGroup rocketConeTG = new TransformGroup(rocketConeTransform);
		Cone cone = new Cone(0.1f, 0.2f);
		rocketConeTG.addChild(cone);
		rocketAssembly.addChild(rocketConeTG);

		// Rocket blade one - Flat, square Cuboid
		Transform3D rocketBlade1Transform = new Transform3D();
		rocketBlade1Transform.setTranslation(new Vector3d(0, -0.20, 0));
		TransformGroup rocketBlade1TG = new TransformGroup(
				rocketBlade1Transform);
		Box cube = new Box(0.15f, 0.05f, 0.01f, blueColour);
		rocketBlade1TG.addChild(cube);
		rocketAssembly.addChild(rocketBlade1TG);

		// Rocket blade two - Flat, square Cuboid
		Transform3D rocketBlade2Transform = new Transform3D();
		rocketBlade2Transform.setTranslation(new Vector3d(0, -0.20, 0));
		TransformGroup rocketBlade2TG = new TransformGroup(
				rocketBlade2Transform);
		Box cube2 = new Box(0.01f, 0.05f, 0.15f, blueColour);
		rocketBlade2TG.addChild(cube2);
		rocketAssembly.addChild(rocketBlade2TG);

		// Window cylinder laying on x axis
		Transform3D window = new Transform3D();
		window.setTranslation(new Vector3d(0, 0.5, 0));
		window.rotX(Math.PI / 2);
		TransformGroup windowTG = new TransformGroup(window);
		Cylinder windowCylinder = new Cylinder(0.05f, 0.2f, blueColour);
		windowTG.addChild(windowCylinder);
		rocketAssembly.addChild(windowTG);

		// LIGHTING On UNIVERSE---------------------------------------------
		BranchGroup bgLight = new BranchGroup();
		// Set up the global lights
		// first the ambient light
		// light from all directions
		// typically use white or 'gray' light
		Color3f alColor = new Color3f(0.6f, 0.6f, 0.6f);
		AmbientLight aLgt = new AmbientLight(alColor);
		aLgt.setInfluencingBounds(bounds);
		bgLight.addChild(aLgt);

		// directional light
		// parallel light rays come FROM infinity TOWARDS the vector lightDir1
		Color3f lightColour1 = new Color3f(1.0f, 1.0f, 1.0f);

		Vector3f lightDirection1 = new Vector3f(-1.0f, 0.0f, -0.5f);
		DirectionalLight dirLight = new DirectionalLight(lightColour1,
				lightDirection1);

		dirLight.setInfluencingBounds(bounds);
		bgLight.addChild(dirLight);

		Vector3f lightDir2 = new Vector3f(1.0f, -1.0f, 0.5f);
		DirectionalLight light2 = new DirectionalLight(lightColour1, lightDir2);
		light2.setInfluencingBounds(bounds);
		bgLight.addChild(light2);

		universe.addBranchGraph(bgLight);

		// END OF LIGHTING--------------------------------------------------

		// COLLISION DETECTION----------------------------------------------
		// Movement of rocket
		// This transformation group is needed for the movement of the rocket.

		TransformGroup rocketMovement = new TransformGroup();
		rocketMovement.addChild(rocketAssembly);

		Transform3D rotationX = new Transform3D();
		rotationX.rotX(Math.PI / 2);
		rocketMovement.setTransform(rotationX);
		//
		// // These properties are needed to allow the cube to moved around the
		// // scene.
		// // *** PICK allows the cube to be "picked up" with the mouse, and
		// moved
		// // around.
		// rocketMovement.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		// rocketMovement.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		// rocketMovement.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		//
		// // This transformation group is needed for the movement of the
		// cylinder.
		// TransformGroup tgRockMove = new TransformGroup();
		// tgRockMove.addChild(rocketMovement);
		//
		// // The movement from up to down (along the y axis)
		// Transform3D yAxis = new Transform3D();
		// // EXERCISE: try getting the cylinder to move up and down (along the
		// // z axis)
		// float maxUp = 0.5f;
		//
		// // An alpha for the up to down
		// // movement***********************************
		// // Alpha(number of times for movement, time movement takes)
		// Alpha rocketUp = new Alpha(2, 2000);
		// // The starting time is first postponed until "infinity".
		// rocketUp.setStartTime(Long.MAX_VALUE);
		// // The interpolator for the movement.
		// // PosInt(theAlpha, TGroup_to_attach_to,
		// // axis_of_movement_default_X_Axis, start_position, end_position)
		// PositionInterpolator rockMoveUp = new PositionInterpolator(rocketUp,
		// rocketMovement, yAxis, 0.0f, maxUp);
		// // An alpha for the up to down movement
		// Alpha rocketDown = new Alpha(1, 2000);
		// // The starting time is first postponed until "infinity".
		// rocketDown.setStartTime(Long.MAX_VALUE);
		// // The interpolator for the movement.
		// PositionInterpolator rockMoveDown = new PositionInterpolator(
		// rocketDown, tgRockMove, yAxis, maxUp, 0.0f);
		//
		// rockMoveUp.setSchedulingBounds(bounds);
		// rockMoveDown.setSchedulingBounds(bounds);
		//
		// // Add the movements to the transformation group.
		// tgRockMove.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		// tgRockMove.addChild(rockMoveUp);
		// tgRockMove.addChild(rockMoveDown);
		//
		// // *** A Switch for the green and the red and the blue spheres
		// Switch colourSwitch = new Switch();
		// colourSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);
		//
		// // creating blue colour
		// Appearance redColour = new Appearance();
		// Color3f redColor = new Color3f(0.6f, 0.0f, 0.0f);
		// ColoringAttributes redCA = new ColoringAttributes();
		// redCA.setColor(redColor);
		// redColour.setColoringAttributes(redCA);
		//
		// // Rocket body cylinder
		// Transform3D rocketTransform2 = new Transform3D();
		// rocketTransform2.setTranslation(new Vector3d(0, 0, -2));
		// TransformGroup rocketAssembly2 = new
		// TransformGroup(rocketTransform2);
		// Cylinder rocketCylinder2 = new Cylinder(0.1f, 0.5f);
		// rocketAssembly2.addChild(rocketCylinder2);
		//
		// // Rocket cone
		// Transform3D rocketConeTransform2 = new Transform3D();
		// rocketConeTransform2.setTranslation(new Vector3d(0, 0.35, 0));
		// TransformGroup rocketConeTG2 = new
		// TransformGroup(rocketConeTransform2);
		// Cone cone2 = new Cone(0.1f, 0.2f, redColour);//
		// *****************************
		// rocketConeTG2.addChild(cone2);
		//
		// // Rocket blade one - Flat, square Cuboid
		// Transform3D rocketBlade1Transform2 = new Transform3D();
		// rocketBlade1Transform2.setTranslation(new Vector3d(0, -0.20, 0));
		// TransformGroup rocketBlade1TG2 = new TransformGroup(
		// rocketBlade1Transform2);
		// Box cubeOne = new Box(0.15f, 0.05f, 0.01f, redColour);
		// rocketBlade1TG.addChild(cubeOne);
		//
		// // Rocket blade two - Flat, square Cuboid
		// Transform3D rocketBlade2Transform2 = new Transform3D();
		// rocketBlade2Transform2.setTranslation(new Vector3d(0, -0.20, 0));
		// TransformGroup rocketBlade2TG2 = new TransformGroup(
		// rocketBlade2Transform2);
		// Box cubeTwo = new Box(0.01f, 0.05f, 0.15f, redColour);
		// rocketBlade2TG.addChild(cubeTwo);
		//
		// // Window cylinder laying on x axis
		// Transform3D window2 = new Transform3D();
		// window2.setTranslation(new Vector3d(0, 0, 0));
		// window2.rotX(Math.PI / 2);
		// TransformGroup windowTG2 = new TransformGroup(window2);
		// Cylinder windowCylinder2 = new Cylinder(0.05f, 0.2f, redColour);
		// windowTG2.addChild(windowCylinder2);
		//
		// // Assembly of rocket
		// rocketAssembly2.setCollidable(false);
		// rocketAssembly2.addChild(rocketConeTG2);
		// rocketAssembly2.addChild(rocketBlade1TG2);
		// rocketAssembly2.addChild(rocketBlade2TG2);
		// rocketAssembly2.addChild(windowTG2);
		//
		// // The Switch node controls which of its children will be rendered.
		// // Add the rocket to the Switch.
		// colourSwitch.addChild(rocketAssembly); // child 0
		// colourSwitch.addChild(rocketAssembly2);
		//
		// // The green sphere should be visible in the beginning.
		// colourSwitch.setWhichChild(1);
		//
		// // A transformation group for the Switch (the rockets).
		// Transform3D tfRocket = new Transform3D();
		// tfRocket.setTranslation(new Vector3f(0.7f, 0.0f, 0.0f));
		// TransformGroup tgRocket = new TransformGroup(tfRocket);
		// tgRocket.addChild(colourSwitch);
		//
		// // ------------------ Begin navigation --
		// // In order to allow navigation through the scene with the keyboard,
		// // everything must be collected in a separate transformation group to
		// // which
		// // the KeyNavigatorBehavior is applied.
		// KeyNavigatorBehavior knb = new KeyNavigatorBehavior(mainTG);
		// knb.setSchedulingBounds(bounds);
		// mainTG.addChild(knb);
		// objRoot.addChild(mainTG);
		//
		// // The PickTranslateBehavior for moving the
		// rocket/cube.***************
		// PickTranslateBehavior pickTrans = new PickTranslateBehavior(objRoot,
		// c,
		// bounds);
		// objRoot.addChild(pickTrans);
		// // ------------------ End navigation --
		//
		// /* *** COLLISION DETECTION --------------------------------- */
		//
		// double cc = 1.0;
		// rocketAssembly.setCollisionBounds(new BoundingSphere(new Point3d(0.0,
		// 0.0, 0.0), cc * 0.1));
		//
		// rocketAssembly.setCollidable(true);
		// // The CollisionBounds for the cylinder.
		// rocketAssembly.setCollisionBounds(new BoundingBox(new Point3d(0.0,
		// -0.15, 0.0), new Point3d(0.1, 0.15, 0.1)));
		// rocketAssembly.setCollidable(true);
		//
		// // CollisionBehaviour2 class takes care of the movement(s) of the
		// // cylinder.
		// Alpha[] rockAlphas = new Alpha[2];
		// rockAlphas[0] = rocketUp;
		// rockAlphas[1] = rocketDown;
		// CollisionBehaviour2 rcb2 = new CollisionBehaviour2(earthSphere,
		// rockAlphas,// *************************
		// bounds);
		//
		// mainTG.addChild(rcb2);

		// /* END COLLISION DETECTION --------------------------------- */

		mainTG.addChild(rocketMovement);

		// End of Rocket----------------------------------------------------

		// Placing all objects and planets onto the scene
		// SUN
		mainTG.addChild(sunBranch);
		sunBranch.addChild(sunTFGroup);
		sunTFGroup.addChild(sunSphere);
		sunTFGroup.addChild(light1OnSun);
		sunTFGroup.addChild(ambientLight);

		// MERCURY
		mainTG.addChild(mercuryBranch);
		mercuryBranch.addChild(mercuryTG);
		mercuryTG.addChild(mercuryTG1);
		mercuryTG1.addChild(mercuryTG2);

		mercuryTG.addChild(mercurySpin);
		mercuryTG1.addChild(mercurySpin1);
		mercuryTG2.addChild(mercSphere);

		// VENUS
		mainTG.addChild(venusBranch);
		venusBranch.addChild(venusTG);
		venusTG.addChild(venusTG1);
		venusTG1.addChild(venusTG2);

		venusTG.addChild(venusSpin);
		venusTG1.addChild(venusSpin1);
		venusTG2.addChild(venusSphere);

		// EARTH
		mainTG.addChild(earthBranch);
		earthBranch.addChild(earthTG);
		earthTG.addChild(earthTG1);
		earthTG1.addChild(earthTG2);

		earthTG.addChild(earthSpin);
		earthTG1.addChild(earthSpin1);
		earthTG2.addChild(earthSphere);

		// MOON
		earthTG1.addChild(moonBranch);
		moonBranch.addChild(moonTG);
		moonTG.addChild(moonSpin);
		moonTG.addChild(moonTG1);
		moonTG1.addChild(moonSpin1);
		moonTG1.addChild(moonTG2);
		moonTG2.addChild(moonSphere);

		// MARS
		mainTG.addChild(marsBranch);
		marsBranch.addChild(marsTG);
		marsTG.addChild(marsTG1);
		marsTG1.addChild(marsTG2);

		marsTG.addChild(marsSpin);
		marsTG1.addChild(marsSpin1);
		marsTG2.addChild(marsSphere);

		// JUPITER
		mainTG.addChild(jupiterBranch);
		jupiterBranch.addChild(jupiterTG);
		jupiterTG.addChild(jupiterTG1);
		jupiterTG1.addChild(jupiterTG2);

		jupiterTG.addChild(jupiterSpin);
		jupiterTG1.addChild(jupiterSpin1);
		jupiterTG2.addChild(jupiterSphere);

		// SATURN
		mainTG.addChild(saturnBranch);
		saturnBranch.addChild(saturnTG);
		saturnTG.addChild(saturnTG1);
		saturnTG1.addChild(saturnTG2);

		saturnTG.addChild(saturnSpin);
		saturnTG1.addChild(saturnSpin1);
		saturnTG2.addChild(saturnSphere);

		// SATURN's rings
		saturnDustTG.addChild(saturnRing);
		saturnDustTG.addChild(makeRingHollow);
		saturnTG2.addChild(saturnDustTG);

		// URANUS
		mainTG.addChild(uranusBranch);
		uranusBranch.addChild(uranusTG);
		uranusTG.addChild(uranusTG1);
		uranusTG1.addChild(uranusTG2);

		uranusTG.addChild(uranusSpin);
		uranusTG1.addChild(uranusSpin1);
		uranusTG2.addChild(uranusSphere);

		// NEPTUNE
		mainTG.addChild(neptuneBranch);
		neptuneBranch.addChild(neptuneTG);
		neptuneTG.addChild(neptuneTG1);
		neptuneTG1.addChild(neptuneTG2);

		neptuneTG.addChild(neptuneSpin);
		neptuneTG1.addChild(neptuneSpin1);
		neptuneTG2.addChild(neptuneSphere);

		// PLUTO
		mainTG.addChild(plutoBranch);
		plutoBranch.addChild(plutoTG);
		plutoTG.addChild(plutoTG1);
		plutoTG1.addChild(plutoTG2);

		plutoTG.addChild(plutoSpin);
		plutoTG1.addChild(plutoSpin1);
		plutoTG2.addChild(plutoSphere);

		// Create the rotate behaviour node
		MouseRotate behavior = new MouseRotate();
		behavior.setTransformGroup(mainTG);
		objRoot.addChild(behavior);
		behavior.setSchedulingBounds(bounds);

		// Create the zoom behaviour node
		MouseZoom behavior2 = new MouseZoom();
		behavior2.setTransformGroup(mainTG);
		objRoot.addChild(behavior2);
		behavior2.setSchedulingBounds(bounds);

		// Create the translate behaviour node
		MouseTranslate behavior3 = new MouseTranslate();
		behavior3.setTransformGroup(mainTG);
		objRoot.addChild(behavior3);
		behavior3.setSchedulingBounds(bounds);

		// Adding stars to the universe background
		TextureLoader spaceBackgroundLoader = new TextureLoader("stars.png",
				this.getContentPane());
		ImageComponent2D spaceImage = spaceBackgroundLoader.getImage();
		Background spaceBackground = new Background();
		spaceBackground.setImage(spaceImage);
		spaceBackground.setApplicationBounds(bounds);
		spaceBackground.setCapability(Background.ALLOW_IMAGE_WRITE);
		objRoot.addChild(spaceBackground);

		objRoot.addChild(mainTG);
		objRoot.compile();
		return objRoot;
	}

	public static void main(String[] args) {
		new MainFrame(new Example3D(), 512, 512);
	}

}
//