#bin/bash

cd $(dirname $0)
java --module-path libs/ --add-modules javafx.controls,javafx.base,javafx.graphics,javafx.fxml -jar libs/logiciel_coiffure-1.0.jar