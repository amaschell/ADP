package fr.upmc.algav.tests;

import fr.upmc.algav.hybridtries.HybridTrie;
import fr.upmc.algav.interfaces.ITrie;
import fr.upmc.algav.patriciatries.Alphabet;
import fr.upmc.algav.patriciatries.IPatriciaTrie;
import fr.upmc.algav.patriciatries.PatriciaTrie;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.Assert.*;

public class PatriciaTrieTest extends AbstractTrieTest {
	private PatriciaTrie patriciaTrie;

	private static final long EXPECTED_NULL_POINTER_COUNT = 2287;

	private static final String WORD_OVER_SEVERAL_EDGES = "ROMANE";
	private static final String WORD_AT_ONLY_ON_EDGE = "hello";
	private static final String WORD_WITH_RESULT_ONLY_EDGE = "RUB";
	private static final String NOT_EXISTING_WORD = "NOTINTRIE";
	private static final String NEW_INSERTED_WORD = "IAmNew";

	private static final String PREFIX_PARTLY_IN_TREE_OVER_EDGES = "ROM";
	private static final String PREFIX_PARTLY_IN_TREE_ONE_EDGE = "ULUS";

    private static final String PREFIX_COUNT_FOR_SEVERAL = "ROM";
    private static final int PREFIX_COUNT_FOR_SEVERAL_COUNT = 4;
    private static final String PREFIX_COUNT_WITH_RESULT_ONLY = "RUB";
    private static final int PREFIX_COUNT_WITH_RESULT_ONLY_COUNT = 5;
    private static final String PREFIX_COUNT_ONE_LETTER_PREFIX = "R";
    private static final int PREFIX_COUNT_ONE_LETTER_PREFIX_COUNT = 9;
    private static final String PREFIX_COUNT_FOR_NO_WORD = "RAN";
    private static final String PREFIX_COUNT_NO_WORDS_AS_WORD_ITSELF = "hello";
    private static final int PREFIX_COUNT_NO_WORDS_COUNT = 0;

    private static final int WORD_COUNT_BEFORE_REMOVAL = 10;
    private static final int WORD_COUNT_AFTER_REMOVAL = 9;
    private static final String REMOVED_WORD_WITH_MERGE = "ROMANE";
    private static final String REMOVED_WORD_WITH_MERGE_AND_RESULT_ONLY_EDGE = "ROMULUS";
    private static final String REMOVED_WORD_FULL_AT_ROOT = "hello";
    private static final String REMOVED_WORD_WITHOUT_MERGE = "RUB";
    private static final String REMOVED_WORD_NOT_EXISTING = "LAMBDA";
    private static final String REMOVED_WORD_NOT_EXISTING_POSSIBLE_RESULT_ONLY_EDGE = "ROM";

	private static final int TRIE_HEIGHT = 4;

    // Total depth of leaves = 36. Total count of leaves = 10
    private static final double AVERAGE_DEPTH_LEAVES = 3.6;

	private static final String[] TEST_DATA = {
			"ROMANE", "ROMANUS", "ROMULUS", "RUBENS", "RUBER",
			"RUBICON", "RUBICUNDUS", "RUB", "hello",
			"ROMULUSBBB", "ROMANE", "ROMANE", "RUBER",
			"ROMANE", "RUB"
	};

	private static final int MERGE_WORD_COUNT_AFTER_MERGE = 14;

    private static final String[] MERGE_TRIE_WORDS = {
            "ROMANE", "BALL", "BRING" , "helium", "total"
    };

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {}

	@Before
	public void setUp() throws Exception {
		patriciaTrie = new PatriciaTrie(new Alphabet());
		insertTestData();
	}

	private void insertTestData() {
		for (int i = 0; i < TEST_DATA.length; i++) {
			patriciaTrie.insert(TEST_DATA[i]);
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void search_existingWord_overSeveralEdges() {
		boolean wordInTrie = patriciaTrie.search(WORD_OVER_SEVERAL_EDGES);
		assertTrue(wordInTrie);
	}

	@Test
	public void search_existingWord_atOnlyOneEdge() {
		boolean wordInTrie = patriciaTrie.search(WORD_AT_ONLY_ON_EDGE);
		assertTrue(wordInTrie);
	}

	@Test
	public void search_existingWord_withResultOnlyEdge() {
		boolean wordInTrie = patriciaTrie.search(WORD_WITH_RESULT_ONLY_EDGE);
		assertTrue(wordInTrie);
	}

	@Test
	public void search_nonExistingWord_completelyNotInTrie() {
		boolean wordInTrie = patriciaTrie.search(NOT_EXISTING_WORD);
		assertFalse(wordInTrie);
	}

	@Test
	public void search_nonExistingWord_prefixPartlyInTrieOverEdges() {
		boolean wordInTrie = patriciaTrie.search(PREFIX_PARTLY_IN_TREE_OVER_EDGES);
		assertFalse(wordInTrie);
	}

	@Test
	public void search_nonExistingWord_prefixPartlyInTrieAtOneEdge() {
		boolean wordInTrie = patriciaTrie.search(PREFIX_PARTLY_IN_TREE_ONE_EDGE);
		assertFalse(wordInTrie);
	}

	@Test
	public void insert_newWord() {
		assertFalse(patriciaTrie.search(NEW_INSERTED_WORD));
		patriciaTrie.insert(NEW_INSERTED_WORD);
		assertTrue(patriciaTrie.search(NEW_INSERTED_WORD));
	}

	@Test
	public void getPrefixCount_normalPrefixForSeveralWords() {
        int prefixCount = patriciaTrie.getPrefixCount(PREFIX_COUNT_FOR_SEVERAL);
		assertEquals(PREFIX_COUNT_FOR_SEVERAL_COUNT, prefixCount);
	}

    @Test
    public void getPrefixCount_noPrefixAsStoredWord() {
        int prefixCount = patriciaTrie.getPrefixCount(PREFIX_COUNT_NO_WORDS_AS_WORD_ITSELF);
        assertEquals(PREFIX_COUNT_NO_WORDS_COUNT, prefixCount);
    }

    @Test
    public void getPrefixCount_prefixForWordEndingInResultOnlyEdge() {
        int prefixCount = patriciaTrie.getPrefixCount(PREFIX_COUNT_WITH_RESULT_ONLY);
        assertEquals(PREFIX_COUNT_WITH_RESULT_ONLY_COUNT, prefixCount);
    }

    @Test
    public void getPrefixCount_oneLetterPrefix() {
        int prefixCount = patriciaTrie.getPrefixCount(PREFIX_COUNT_ONE_LETTER_PREFIX);
        assertEquals(PREFIX_COUNT_ONE_LETTER_PREFIX_COUNT, prefixCount);
    }

    @Test
    public void getPrefixCount_prefixForNoWord() {
        int prefixCount = patriciaTrie.getPrefixCount(PREFIX_COUNT_FOR_NO_WORD);
        assertEquals(PREFIX_COUNT_NO_WORDS_COUNT, prefixCount);
    }

	@Test
	public void remove_wordWithMerge() {
        boolean wasSuccess = patriciaTrie.remove(REMOVED_WORD_WITH_MERGE);

        assertTrue(wasSuccess);
        assertFalse(patriciaTrie.search(REMOVED_WORD_WITH_MERGE));
        assertEquals(WORD_COUNT_AFTER_REMOVAL, patriciaTrie.getWordCount());
	}

	@Test
	public void remove_wordWithMergeAndResultOnlyEdge() {
        boolean wasSuccess = patriciaTrie.remove(REMOVED_WORD_WITH_MERGE_AND_RESULT_ONLY_EDGE);

        assertTrue(wasSuccess);
        assertFalse(patriciaTrie.search(REMOVED_WORD_WITH_MERGE_AND_RESULT_ONLY_EDGE));
        assertEquals(WORD_COUNT_AFTER_REMOVAL, patriciaTrie.getWordCount());
	}

    @Test
    public void remove_fullWordAtRoot() {
        boolean wasSuccess = patriciaTrie.remove(REMOVED_WORD_FULL_AT_ROOT);

        assertTrue(wasSuccess);
        assertFalse(patriciaTrie.search(REMOVED_WORD_FULL_AT_ROOT));
        assertEquals(WORD_COUNT_AFTER_REMOVAL, patriciaTrie.getWordCount());
    }

    @Test
    public void remove_wordWithoutMerge() {
        boolean wasSuccess = patriciaTrie.remove(REMOVED_WORD_WITHOUT_MERGE);

        assertTrue(wasSuccess);
        assertFalse(patriciaTrie.search(REMOVED_WORD_WITHOUT_MERGE));
        assertEquals(WORD_COUNT_AFTER_REMOVAL, patriciaTrie.getWordCount());
    }

    @Test
    public void remove_wordNotExisting() {
        boolean wasSuccess = patriciaTrie.remove(REMOVED_WORD_NOT_EXISTING);

        assertFalse(wasSuccess);
        assertFalse(patriciaTrie.search(REMOVED_WORD_NOT_EXISTING));
        assertEquals(WORD_COUNT_BEFORE_REMOVAL, patriciaTrie.getWordCount());
    }

    @Test
    public void remove_wordNotExistingWithPossibleResultOnlyEdge() {
        boolean wasSuccess = patriciaTrie.remove(REMOVED_WORD_NOT_EXISTING_POSSIBLE_RESULT_ONLY_EDGE);

        assertFalse(wasSuccess);
        assertFalse(patriciaTrie.search(REMOVED_WORD_NOT_EXISTING_POSSIBLE_RESULT_ONLY_EDGE));
        assertEquals(WORD_COUNT_BEFORE_REMOVAL, patriciaTrie.getWordCount());
    }

	@Test
	public final void runNominalTestIsEmpty() {
		PatriciaTrie tempTrie = new PatriciaTrie(new Alphabet());
        assertTrue(tempTrie.isEmpty());
	}
	
	@Test
	public final void runNominalTestRemoveAll() {
		// TODO
	}

	@Test
	public final void runNominalTestInsert() {
		// TODO
	}

	@Test
	public final void runNominalTestSearch() {
		// TODO
	}

	@Test
	public final void runNominalTestCountWords() {
		HashSet<String> wordsSet = new HashSet<>();

		for (String word : TEST_DATA) {
			wordsSet.add(word);
		}

		assertEquals(wordsSet.size(), patriciaTrie.getWordCount());
	}

	@Test
	public final void runNominalTestListWords() {
		ArrayList<String> storedWords = patriciaTrie.getStoredWords();

		for (String word : TEST_DATA) {
			assertTrue(storedWords.contains(word));
		}
	}

	@Test
	public final void runNominalTestCountNull() {
		assertEquals(EXPECTED_NULL_POINTER_COUNT, patriciaTrie.getNullPointerCount());
	}

	@Test
	public final void runNominalTestHeight() {
		assertEquals(TRIE_HEIGHT, patriciaTrie.getHeight());
	}

	@Test
	public final void runNominalTestAverageDepth() {
        assertEquals(AVERAGE_DEPTH_LEAVES, patriciaTrie.getAverageDepthOfLeaves(), 0);
	}

	@Test
	public final void runNominalTestPrefix() {
		// TODO
	}

	@Test
	public final void runNominalTestRemove() {
		// TODO
	}
	
	@Test
	public final void runNominalTestPrint() {
		// TODO
	}

	@Test
	public final void runNominalTestMerge() {
		PatriciaTrie otherTrie = new PatriciaTrie(new Alphabet());

        for (String otherTrieWord : MERGE_TRIE_WORDS) {
            otherTrie.insert(otherTrieWord);
        }

        IPatriciaTrie mergedTrieThisOther = patriciaTrie.merge(otherTrie);
        IPatriciaTrie mergedTrieOtherThis = otherTrie.merge(patriciaTrie);

        assertEquals(MERGE_WORD_COUNT_AFTER_MERGE, mergedTrieThisOther.getWordCount());
        assertEquals(MERGE_WORD_COUNT_AFTER_MERGE, mergedTrieOtherThis.getWordCount());

        for (String trieWord : TEST_DATA) {
            assertTrue(mergedTrieThisOther.search(trieWord));
            assertTrue(mergedTrieOtherThis.search(trieWord));
        }

        for (String otherTrieWord : MERGE_TRIE_WORDS) {
            assertTrue(mergedTrieThisOther.search(otherTrieWord));
            assertTrue(mergedTrieOtherThis.search(otherTrieWord));
        }
    }

	@Test
	public final void runNominalTestToHybridTrie() {
		// TODO
	}
}
