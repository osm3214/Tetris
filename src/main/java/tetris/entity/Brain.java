package tetris.entity;

import java.util.Random;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import ai.djl.*;
import ai.djl.inference.*;
import ai.djl.ndarray.*;
import ai.djl.translate.*;

public class Brain {

    private Model model;
    Translator<int[][], Float> translator;
    Predictor<int[][], Float> predictor;

    public Brain() {
        Path modelDir = Paths.get("resources/models/");

        model = Model.newInstance("tetrisnet");
        try {
            model.load(modelDir);
        } catch (Exception e) {
            e.printStackTrace();
        }

        translator = new Translator<int[][], Float>(){
            @Override
            public NDList processInput(TranslatorContext ctx, int[][] input) {
                NDManager manager = ctx.getNDManager();
                NDArray array = manager.create(input);
                return new NDList(array);
            }

            @Override
            public Float processOutput(TranslatorContext ctx, NDList list) {
                NDArray temp_arr = list.get(0);
                return temp_arr.getFloat();
            }

            @Override
            public Batchifier getBatchifier() {
                // The Batchifier describes how to combine a batch together
                // Stacking, the most common batchifier, takes N [X1, X2, ...] arrays to a single [N, X1, X2, ...] array
                return Batchifier.STACK;
            }
        };

        predictor = model.newPredictor(translator);
    }

    public int selectAction(ArrayList<int[][]> nextStates) {
        Random r = new Random();
        int idx = r.nextInt(nextStates.size());

        return idx;
    }
}
