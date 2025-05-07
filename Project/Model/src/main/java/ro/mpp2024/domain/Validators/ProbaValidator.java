package ro.mpp2024.domain.Validators;

import ro.mpp2024.domain.Proba;

import java.util.ArrayList;
import java.util.List;

public class ProbaValidator implements Validator<Proba>{
    @Override
    public void validate(Proba proba) throws ValidationException {
        List<Integer> distances = new ArrayList<>(List.of(50,200,800,1500));
        List<String> styles = new ArrayList<>(List.of("liber", "spate","fluture", "mixt"));
        String err = "";
        if(!distances.contains(proba.getDistanta()))
            err += "Distanta invalida!\n";

        if(!styles.contains(proba.getStil()))
            err += "Stil invalid!\n";

        if(!err.isEmpty())
            throw new ValidationException(err);
    }
}
