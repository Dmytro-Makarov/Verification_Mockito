import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import uni.makarov.verification.LetterRepetitionExclusion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class MockitoTests {

    @Mock
    private Scanner scanner;

    @InjectMocks
    private static LetterRepetitionExclusion repetitionExclusion;

    @Spy
    ArrayList<String> processedWords = new ArrayList<>();

    @Spy
    private LetterRepetitionExclusion repetitionExclusionSpy;

    @BeforeClass
    public void init(){
        MockitoAnnotations.openMocks(this);
    }

    @BeforeMethod
    public void checkMock(){
        assertNotNull(scanner);
    }

    public String input = "MOOOOO!1!!!1! \n\n scar?y, is it ???? \t\t crazy. cow!";

    @BeforeMethod
    public void setUp() {
        when(scanner.nextLine()).thenReturn(input);
    }

    @Test
    public void unicodeLatinTest(){
        String input = "Lòrem ipsùm dolor sit amet 123, co#$nsectetur [adipiscing èlit,";
        ArrayList<String> result = repetitionExclusion.removeLetterRepeatingWords(input);
        ArrayList<String> expected = new ArrayList<>(List.of("Lorem ipsum sit amet elit".split(" ")));
        assertEquals(expected, result);
    }

    @Test
    public void specialCharactersTest(){
        String input = "!#$k$@$^#^@\\{a@$!#$n&#$r%@%y@^@o^$&ch#&i?|>>/|\"";
        ArrayList<String> result = repetitionExclusion.removeLetterRepeatingWords(input);
        ArrayList<String> expected = new ArrayList<>(List.of("kanryochi".split(" ")));
        assertEquals(expected, result);
    }

    @Test
    public void annotationsTest() {
        ArrayList<String> expected = new ArrayList<>(List.of("scary is it crazy cow".split(" ")));
        ArrayList<String> result = repetitionExclusion.removeLetterRepeatingWords(scanner.nextLine());
        assertEquals(expected, result);
    }

    @Test
    public void testSpyMock() {
        ArrayList<String> result = repetitionExclusionSpy.removeLetterRepeatingWords("");
        assertEquals(Collections.emptyList(), result);
    }

    @Test(priority = 1)
    public void testCalledTimes(){
        //String input = "do-re mi-fa";
        repetitionExclusionSpy.removeLetterRepeatingWords(input);
        verify(repetitionExclusionSpy, atLeastOnce()).removeLetterRepeatingWords(input);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testException() {
        ArrayList<String> result = repetitionExclusion.removeLetterRepeatingWords(null);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testThrowingException(){
        doThrow(new RuntimeException()).when(processedWords).add(anyString());
        repetitionExclusion.removeLetterRepeatingWords(null);
    }

    @AfterMethod
    public void validate() {
        validateMockitoUsage();
    }
}
