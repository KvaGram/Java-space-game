package unicus.spacegame;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import org.lwjgl.Version;
import static org.lwjgl.glfw.Callbacks.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
import static java.lang.System.out;

//Main class based on sample code
public class Main {
    private long windowHandle;
    public void run() {
        out.println("Starting LWJGL version " + Version.getVersion());

        try {
            init();
            loop();

            //release and destroy window
            glfwFreeCallbacks(windowHandle);
            glfwDestroyWindow(windowHandle);
        } finally {
            //From sample code, not sure yet what it does.
            // from sample code: 'Terminate GLFW and release the GLFWerrorfun'
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
    }
    private void init() {
        //sets errors to be printed to the error feed.
        GLFWErrorCallback.createPrint(System.err).set();

        //From sample code. Do not yet know what GLFW is. Clearly it is vital.
        //This initiates GLFW. If it fails, the program should exit.
        if(!glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW");
        }
        //Window configuration
        //glfwDefaultWindowHints(); //default, not needed to specify.
        glfwWindowHint(GLFW_VISIBLE, GL_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

        //set pixel width and height
        int WIDTH = 300;
        int HEIGHT = 300;

        // Create game window
        // If the game window fails, the program should exit.
        windowHandle = glfwCreateWindow(WIDTH, HEIGHT, "The Homecomer (pre-alpha)", 0, 0);
        if (windowHandle == NULL) {
            throw new RuntimeException("Creation of GLFW window failed");
        }

        //set up listener and callback for releasing esc-key.
        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            }
        });

        //Get data (size, index, address, display-data, etc.)
        GLFWVidMode screenData = glfwGetVideoMode(glfwGetPrimaryMonitor());
        //center window on screen
        glfwSetWindowPos(windowHandle,
                (screenData.width() - WIDTH)/2,
                (screenData.height() - HEIGHT)/2
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowHandle);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(windowHandle);
    }
    private void loop() {
        //important behind-the-scenes-thing I have no clue on what is doing
        //apparently related to LWJGL
        GL.createCapabilities();

        //Sets color to clear the screen (grey)
        glClearColor(.5f,.5f,.5f,0);

        while(!glfwWindowShouldClose(windowHandle)) {
            //Clear the screen. No idea what the mask is for.
            //Is the buffer layered?
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // setup viewing projection
            glMatrixMode(GL_PROJECTION);
            //setup matrix
            glLoadIdentity();
            //setup orthographic perspective
            glOrtho(0.0, 10.0, 0.0, 10.0, -1.0, 1.0);   // setup a 10x10x2 viewing world

            //space where you draw stuff
            testDraw();

            //Let the system/OpenGL know you are done drawing on this screen/buffer.
            glfwSwapBuffers(windowHandle);

            //Runs the event-driver.
            glfwPollEvents();
        }
    }

    private void testDraw() {

        glColor3f(0.0f, 1.0f, 0.0f);
        glBegin(GL_POLYGON);
        glVertex3f(2.0f, 2.0f, 0.0f);
        glVertex3f(8.0f, 2.0f, 0.0f);
        glVertex3f(8.0f, 8.0f, 0.0f);
        glVertex3f(2.0f, 8.0f, 0.0f);
        glEnd();
        glFlush();
    }


    public static void main(String[] args) {
        new Main().run();
    }

}
