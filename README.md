# Reddit Viewer

This is a test project I'm using to slowly learn React through Reagent and Re-frame.
This branch is the re-framed version of the master branch version.

### Development mode

To start the Figwheel compiler, navigate to the project folder and run the following command in the terminal:

```
lein figwheel
```

Figwheel will automatically push cljs changes to the browser.
Once Figwheel starts up, you should be able to open the `public/index.html` page in the browser.


### Building for production

```
lein clean
lein package
```
