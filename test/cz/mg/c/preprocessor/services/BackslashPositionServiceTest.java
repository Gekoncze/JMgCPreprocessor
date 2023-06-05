package cz.mg.c.preprocessor.services;

import cz.mg.annotations.classes.Test;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.test.Assert;
import cz.mg.tokenizer.entities.Position;
import cz.mg.tokenizer.services.PositionService;

public @Test class BackslashPositionServiceTest {
    public static void main(String[] args) {
        System.out.print("Running " + BackslashPositionServiceTest.class.getSimpleName() + " ... ");

        BackslashPositionServiceTest test = new BackslashPositionServiceTest();
        test.testCorrection();

        System.out.println("OK");
    }

    @SuppressWarnings("ConstantConditions")
    private void testCorrection() {
        String originalContent = "foobar" + "\\" + "\n" +
            "123\\56" + "\\" + "\n" +
            "xyz" + "\\" + "\n" +
            "\\" + "\n" +
            "789" + "\n" +
            "vw";

        String actualContent = "foobar123\\56xyz789\nvw";

        testCorrection(
            originalContent,
            actualContent.indexOf("foobar"),
            1, 1
        );

        testCorrection(
            originalContent,
            actualContent.indexOf("oobar"),
            1, 2
        );

        testCorrection(
            originalContent,
            actualContent.indexOf("r"),
            1, 6
        );

        testCorrection(
            originalContent,
            actualContent.indexOf("123\\56"),
            2, 1
        );

        testCorrection(
            originalContent,
            actualContent.indexOf("23\\56"),
            2, 2
        );

        testCorrection(
            originalContent,
            actualContent.indexOf("6"),
            2, 6
        );

        testCorrection(
            originalContent,
            actualContent.indexOf("x"),
            3, 1
        );

        testCorrection(
            originalContent,
            actualContent.indexOf("y"),
            3, 2
        );

        testCorrection(
            originalContent,
            actualContent.indexOf("z"),
            3, 3
        );

        testCorrection(
            originalContent,
            actualContent.indexOf("7"),
            5, 1
        );

        testCorrection(
            originalContent,
            actualContent.indexOf("8"),
            5, 2
        );

        testCorrection(
            originalContent,
            actualContent.indexOf("9"),
            5, 3
        );

        testCorrection(
            originalContent,
            actualContent.indexOf("v"),
            6, 1
        );

        testCorrection(
            originalContent,
            actualContent.indexOf("w"),
            6, 2
        );
    }

    private void testCorrection(@Mandatory String originalContent, int position, int expectedRow, int expectedColumn) {
        BackslashPositionService service = BackslashPositionService.getInstance();
        int actualCorrectedPosition = service.correct(originalContent, position);
        PositionService positionService = PositionService.getInstance();
        Position actualPosition = positionService.find(originalContent, actualCorrectedPosition);
        Position expectedPosition = new Position(expectedRow, expectedColumn);
        Assert
            .assertThat(expectedPosition, actualPosition)
            .withCompareFunction((a, b) -> a.getRow() == b.getRow() && a.getColumn() == b.getColumn())
            .withPrintFunction(p -> "[" + p.getRow() + "," + p.getColumn() + "]")
            .areEqual();
    }
}
