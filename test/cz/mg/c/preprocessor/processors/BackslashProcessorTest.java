package cz.mg.c.preprocessor.processors;

import cz.mg.annotations.classes.Test;
import cz.mg.test.Assert;

public @Test class BackslashProcessorTest {
    public static void main(String[] args) {
        System.out.print("Running " + BackslashProcessorTest.class.getSimpleName() + " ... ");

        BackslashProcessorTest test = new BackslashProcessorTest();
        test.testProcessingFirst();
        test.testProcessingMiddle();
        test.testProcessingLast();
        test.testMissortedBackslash();

        System.out.println("OK");
    }

    private void testProcessingFirst() {
        BackslashProcessor processor = BackslashProcessor.getInstance();
        String content = processor.process("\\\n69");
        Assert.assertEquals("69", content);
    }

    private void testProcessingMiddle() {
        BackslashProcessor processor = BackslashProcessor.getInstance();
        String content = processor.process("foo\\\nbar\\\n69");
        Assert.assertEquals("foobar69", content);
    }

    private void testProcessingLast() {
        BackslashProcessor processor = BackslashProcessor.getInstance();
        String content = processor.process("69\\\n");
        Assert.assertEquals("69", content);
    }

    private void testMissortedBackslash() {
        BackslashProcessor processor = BackslashProcessor.getInstance();
        String content = processor.process("6\n\\9");
        Assert.assertEquals("6\n\\9", content);
    }
}
