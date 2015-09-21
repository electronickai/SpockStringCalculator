package gui

import calculator.SimpleGroovyStringCalculator

import static groovyx.javafx.GroovyFX.start

/**
 * Created by schmidka on 21.09.2015.
 */

//model
CalculatorModel model = new CalculatorModel()

//"controller"
// works together with service layer no matter whether implemented with groovy or Java
SimpleGroovyStringCalculator calculator = new SimpleGroovyStringCalculator()
//JavaStringCalculator calculator = new JavaStringCalculator() //alternatively
def calculate = {
    calculator.add(model.input)
}

//View
start {
    stage title: 'String Calculator Adder', visible: true, {
        scene {
            hbox {
                text = textField(id: 'calculationString', text:bind(model.inputProperty))
                button(id: 'Show Result', text: 'Show Result', onAction: {model.output = calculate()})
                text = textField(id:'result', text: bind(model.outputProperty))
            }
        }
    }
}
