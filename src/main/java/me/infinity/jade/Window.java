package me.infinity.jade;

import me.infinity.util.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    int width, height;
    String title;
    private long glfwWindow;

    private float a, r, g, b;
    private boolean fadeToBlack;

    private static Window window = null;

    private static Scene currentScene = null;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Mario";
        this.a = 1.0f;
        this.r = 1.0f;
        this.g = 1.0f;
        this.b = 1.0f;
    }

    public static void changeScene(int scene) {
        switch (scene) {
            case 0:
                currentScene = new LevelEditorScene();
                break;
            case 1:
                currentScene = new LevelScene();
                break;
            default:
                assert false : "Unknown scene '" + scene + "'";
        }
    }

    public static Window get() {
        if (window == null) {
            window = new Window();
        }

        return window;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        // Set an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        // Make the OpenGL context
        glfwMakeContextCurrent(glfwWindow);
        // Enable v-sync
        //glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        changeScene(0);
    }

    public void loop() {
        float beginTime = Time.getTime();
        float dt = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow)) {
            // Poll events
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);


            if (dt > 0)
                currentScene.update(dt);

            glfwSwapBuffers(glfwWindow);

            float endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
            // System.out.println((1.0 / dt) + "FPS");
        }
    }

    public void updateRGB(float dx) {
        this.r += dx;
        this.g += dx;
        this.b += dx;
    }

    public void setARGB(int a, int r, int g, int b) {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
    }
}
