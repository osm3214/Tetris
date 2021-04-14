package tetris.entity;

import java.nio.file.Paths;
import java.util.ArrayList;

import ai.djl.*;
import ai.djl.inference.*;
import ai.djl.ndarray.*;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.types.Shape;
import ai.djl.translate.*;

public class Brain {

    private Predictor<int[][], Float> predictor;

    private static final int NUM_ROWS = 20;
    private static final int NUM_COLS = 10;
    private static final String modelDirName = "resources/models";
    private static final String modelName = "tetrisnet";

    public Brain() {
        Model model = Model.newInstance(modelName);
        try {
            model.load(Paths.get(modelDirName));
        } catch (Exception e) {
            e.printStackTrace();
        }

        predictor = model.newPredictor(new Translator<int[][], Float>() {
            @Override
            public NDList processInput(TranslatorContext ctx, int[][] input) {
                NDArray array = ctx.getNDManager().ones(new Shape(1, NUM_ROWS, NUM_COLS));
                for (int y = 0; y < NUM_ROWS; y++) {
                    for (int x = 0; x < NUM_COLS; x++) {
                        array.set(new NDIndex(0, y, x), (float) input[y][x]);
                    }
                }
                return new NDList(array);
            }

            @Override
            public Float processOutput(TranslatorContext ctx, NDList list) {
                return list.get(0).getFloat();
            }

            @Override
            public Batchifier getBatchifier() {
                return Batchifier.STACK;
            }
        });
    }

    public int selectAction(ArrayList<int[][]> nextStates) {
        float maxScore = -100;
        int maxIdx = 0;

        for (int i = 0; i < nextStates.size(); i++) {
            float score = 0;
            int[][] nextState = nextStates.get(i);

            Board.flip(nextState);
            Board.binarize(nextState);

            try {
                score = predictor.predict(nextState);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (score > maxScore) {
                maxScore = score;
                maxIdx = i;
            }
        }
        return maxIdx;
    }
}
