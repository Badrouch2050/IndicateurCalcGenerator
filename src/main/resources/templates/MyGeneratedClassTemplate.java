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

@Service("TOT_BIL")
public class TotalBilan extends AbstractIndicateurCalcul {
    private static final Logger LOGGER = LogManager.getLogger(TotalBilan.class);

    private static final String MESSAGE = "Indicateur Excédent Total Bilan non calculé";
    public static final String MESSAGEEROR = "Error during calculate indicateur : TOT_BIL";
    public static final String MESSAGEEROR2050 = "Error during calculate indicateur  2050 : TOT_BIL ";

    //     * ET Valeur du poste EE est  renseignée
    private static Predicate<Map<String, BigDecimal>> caseOne = x ->
            IsOneOfValuesNotNull(x.get("EE"));

    //  * ET Valeur du poste EE est non renseignée
    //     * ET Valeur du poste CO  est  renseignée
    //     * ET Valeur du poste 1A  est  renseignée
    private static Predicate<Map<String, BigDecimal>> caseTwo = x ->
            !IsOneOfValuesNotNull(x.get("EE")) && IsOneOfValuesNotNull(x.get("CO")) && IsOneOfValuesNotNull(x.get("1A"));
    //     * ET Valeur du poste EE est non renseignée
    //     * ET Valeur du poste CO  est  non renseignée
    //     * ET Valeur du poste 1A  est non renseignée
    //     * ET Valeur du poste CO3  est   renseignée
    private static Predicate<Map<String, BigDecimal>> caseThree = x ->
            !IsOneOfValuesNotNull(x.get("EE"), x.get("CO"), x.get("1A"), x.get("EE")) && IsOneOfValuesNotNull(x.get("CO3"));


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
     * Les critères d'acceptance :
     * CAS 01 :
     * Etant Donné :
     * Réception d'un fichier BEFG001 valide
     * Pour un état financier valide
     * Quand :
     * MODELE_EF="2050"
     * ET Valeur du poste EE est  renseignée
     * <p>
     * Alors :
     * CODE_INDICATEUR = "TOT_BIL"
     * Valeur de l'indicateur TOT_BIL = Valeur du poste EE
     * CAS 02 :
     * Etant Donné :
     * Réception d'un fichier BEFG001 valide
     * Pour un état financier valide
     * Quand :
     * MODELE_EF="2050"
     * ET Valeur du poste EE est non renseignée
     * ET Valeur du poste CO  est  renseignée
     * ET Valeur du poste 1A  est  renseignée
     * Alors :
     * CODE_INDICATEUR = "TOT_BIL"
     * Valeur de l'indicateur TOT_BIL = Valeur du poste CO - Valeur du poste 1A
     * <p>
     * CAS 03 :
     * Etant Donné :
     * Réception d'un fichier BEFG001 valide
     * Pour un état financier valide
     * Quand :
     * MODELE_EF="2050"
     * ET Valeur du poste EE est non renseignée
     * ET Valeur du poste CO  est  non renseignée
     * ET Valeur du poste 1A  est non renseignée
     * ET Valeur du poste CO3  est   renseignée
     * <p>
     * Alors :
     * CODE_INDICATEUR = "TOT_BIL"
     * Valeur de l'indicateur TOT_BIL = Valeur du poste CO3
     * CAS 04 :
     * Etant Donné :
     * Réception d'un fichier BEFG001 valide
     * Pour un état financier valide
     * Quand :
     * MODELE_EF="2050"
     * ET Valeur du poste EE est non renseignée
     * ET Valeur du poste CO  est  non renseignée
     * ET Valeur du poste 1A  est non renseignée
     * ET Valeur du poste CO3  est  non renseignée
     *
     * Alors :
     * Pas de calcul  de l'indicateur TOT_BIL pour l'état financier en question
     * CODE_INDICATEUR = "TOT_BIL"
     * VALEUR : NULL
     * Commentaire : 'Indicateur Total Bilan non calculé'
     *
     * @param allPoste             : Map Code/Valeur des postes d'EF
     * @param indicateurCentralDto Resultat des indicateurs calculés
     * @throws BefException Exception Fonctionnelle BEFException
     */
    private void calculeIndicateurModel2050(Map<String, BigDecimal> allPoste, IndicateurCentralDto indicateurCentralDto) throws BefException {
        try {
            if (caseOne.test(allPoste)) {
                indicateurCentralDto.setValeur(allPoste.get("EE"));
            } else if (caseTwo.test(allPoste)) {
                indicateurCentralDto.setValeur(allPoste.get("CO").subtract(allPoste.get("1A")));
            } else if (caseThree.test(allPoste)) {
                indicateurCentralDto.setValeur(allPoste.get("CO3"));
            } else {
                indicateurCentralDto.setValeur(null);
                indicateurCentralDto.setCommentaire(MESSAGE);
                LOGGER.log(Level.INFO, MESSAGE);
            }

        } catch (Exception e) {
            LOGGER.log(Level.ERROR, MESSAGEEROR2050);
            throw new BefException(MESSAGEEROR2050, Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public String getAbstractIndicateurCalcul() {
        return "TOT_BIL";
    }
}
