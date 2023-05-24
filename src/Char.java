public class Char {
    private final char character;
    private final String binaryRepresentation;

    public Char(char character){
        this.character = character;
        this.binaryRepresentation = toBinary();
    }

    public char getCharacter() {
        return this.character;
    }

    public String getBinaryRepresentation() {
        return this.binaryRepresentation;
    }

    private String toBinary() {
        return String.format("%5s", Integer.toBinaryString(this.character).substring(2))
                .replaceAll(" ", "0");
    }
}
