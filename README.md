# SystemsTest

A high-level framework for managing power across multiple systems in a fictional spaceship.
This is a "for fun" project and a test bed for exploring ideas relating to managing systems on a fictional craft.

The purpose of this project is to create the various systems that might be present on a spaceship and to test how these systems interact, forming a basis for a spaceship management game, heavily inspired by Elite Dangerous.

This is done as an exercise only.

Note: the UI layout and design is currently very basic and needs a lot of work.

# Requirements

Project was created in the Intellij IDEA community edition IDE, and is currently not supported in other IDEs (such as Eclipse) due to dependancies on the Intellij UI Designer. Future work aims to resolve these dependancies; however, here is a link to the IDE for now. <br>

https://www.jetbrains.com/idea/download/<br />

# Running

To run the project, the following command must be executed from within the same directory as the "SystemsTest.jar" file:<br />

java -jar SystemsTest.jar<br />

This ensures the configs are loaded correctly, as there is currently not a way to specify where the program looks for the configs, and as such it expects them in the config directory provided.

# To do

-Create more "systems" once the framework has been set up to allow a system to run: in this case, draw power and perform functions<br />
-Add more detail to systems<br />
-Create a user interface to allow systems to be added and removed by a user at runtime<br />
-Load configurations for various models from disk (partially complete, basic XML configs have been added)<br />
-More error checking and input validation<br />
-Redesign UI to be more modular and pleasing to the eye
