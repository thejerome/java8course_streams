package songs;

import javafx.scene.control.Button;
import org.junit.Assert;

public class Task2 {
    public static void main(String[] args) {

        Dream dream = new Dream();
        Person you = new Person();

        //---------------------------------

        Button theButton = new Button();
        theButton.setOnAction(
                e -> new Result(() -> you.getDream().fulfill())
        );
        Assert.assertFalse(you.isGlad());
        Assert.assertNull(you.getDream());

        //---------------------------------

    }

    private static class Result {
        public Result(Runnable o) {

        }
    }


    private static class Dream {
        public void fulfill() {

        }

    }

    private static class Person {
        private Dream dream;
        private boolean glad;

        public Dream getDream() {
            return dream;
        }

        public void setDream(Dream dream) {
            this.dream = dream;
        }

        public boolean isGlad() {
            return glad;
        }

        public void setGlad(boolean glad) {
            this.glad = glad;
        }
    }
}
