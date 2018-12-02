package com.engine;

class ReadMe {
    /*
    * Matrx Engine - Alpha 0.1.3
    * Classes:
    *  com.Core:
    *   ReadMe - Info about the Core
    *   Core - Main class that runs the entire game, contains game loop
    *   lib:
    *    game:
    *     Game - Class that has to extend your game class, needs update() and render() methods
    *     GameObject - Class that has to extend your game object. It can be added to your game via <yourGameClass>.addObject(GameObject obj)
    *    input:
    *     Input - Reads mouse clicks and mouse movement, keyboard key presses, releases and when key is begging hold down
    *    math:
    *     BasicMath - max(double... args), min(double... args), sqrt(double a), floor(double a), random(int min, int max) ect...
    *     AdvancedMath - inRange, inRadius, distance, direction, lerp ect...
    *    rendering:
    *     surface:
    *      Surface
    *     Font - Class used in Renderer class for drawing fonts and can also be used to define custom fonts in src/resources folder
    *     Image - Like a BufferedImage but simples to use initialize with Image("filename.png"), it uses src/resources folder
    *     ImageFramePackage - Can be used to get subimages from Image and also as animations
    *     Renderer - The core of rendering, use it to draw to your Window or to Surface
    *     Window - You have nothing to with it, seriously, nothing...
    *  resources: - Place for your game assets
    *   standard.png - Do not delete! This is the main font for that Core
    *
    *   -----------------
    *   To exit game
    *   then don't do  only Core.stop(), you have to do System.exit(int exitcode) too!
    * */
}
