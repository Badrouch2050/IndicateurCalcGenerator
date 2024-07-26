package fr.bpce.bef.indicateurs.impl;


import fr.bpce.bef.dto.EtatFinancierDto;
import fr.bpce.bef.dto.IndicateurCentralDto;
import fr.bpce.bef.exceptions.BefException;
import fr.bpce.bef.indicateurs.AbstractIndicateurCalcul;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Predicate;

import static fr.bpce.bef.utils.IndicateursTools.IsOneOfValuesNotNull;

@Service("${CLASS_SERVICE}")
public class ${CLASS_NAME} extends AbstractIndicateurCalcul {
    private static final Logger LOGGER = LogManager.getLogger(${CLASS_NAME}.class);

    private static final String MESSAGE = "Indicateur Excédent ${CLASS_SERVICE} non calculé";
    public static final String MESSAGEEROR = "Error during calculate indicateur : ${CLASS_SERVICE}";
    public static final String MESSAGEEROR2050 = "Error during calculate indicateur  2050 : ${CLASS_SERVICE} ";



    @Override
    public void computeIndicateur(Map<String, BigDecimal> allPoste, EtatFinancierDto etatFinancierDto, IndicateurCentralDto indicateurCentralDto) throws BefException {
        try {
            if ("2050".equals(etatFinancierDto.getModeleEF())) {
                calculeIndicateurModel2050(allPoste, indicateurCentralDto);
            }
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, MESSAGEEROR);
            throw new BefException(MESSAGEEROR, e.getMessage());
        }
    }

    /**
     *
     *
     * @param allPoste             : Map Code/Valeur des postes d'EF
     * @param indicateurCentralDto Resultat des indicateurs calculés
     * @throws BefException Exception Fonctionnelle BEFException
     */

    private void calculeIndicateurModel2050(Map<String, BigDecimal> allPoste, IndicateurCentralDto indicateurCentralDto) throws BefException {
        try {

            }

        } catch (Exception e) {
            LOGGER.log(Level.ERROR, MESSAGEEROR2050);
            throw new BefException(MESSAGEEROR2050, Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public String getAbstractIndicateurCalcul() {
        return "${CLASS_SERVICE}";
    }
}
