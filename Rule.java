public class Rule {
    //Class to make the Rule object with a String axiom and line
    protected String line, axiom;

    public Rule(){
        this.axiom = "";
        this.line = "";
    }

    public Rule(String axiom, String line){
        this.axiom = axiom;
        this.line = line;
    }

    public String getAxiom() {
        return axiom;
    }

    public void setAxiom(String axiom) {
        this.axiom = axiom;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }
}
