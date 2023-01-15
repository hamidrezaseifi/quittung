package de.seifi.rechnung_manager_app.utils;

import de.seifi.rechnung_manager_app.RechnungManagerFxApp;
import de.seifi.rechnung_manager_app.RechnungManagerSpringApp;
import de.seifi.rechnung_manager_app.enums.RechnungType;
import de.seifi.rechnung_manager_app.models.CustomerModel;
import de.seifi.rechnung_manager_app.models.KostenvoranschlagModel;
import de.seifi.rechnung_manager_app.models.RechnungModel;
import de.seifi.rechnung_manager_app.models.print.KostenvoranschlagPrintJRDataSource;
import de.seifi.rechnung_manager_app.models.print.PrintJRDataSourceBase;
import de.seifi.rechnung_manager_app.models.print.QuittungPrintJRDataSource;
import de.seifi.rechnung_manager_app.models.print.RechnungPrintJRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PrintUtils {
    private static final Logger logger = LoggerFactory.getLogger(PrintUtils.class);

    public static void printRechnungItems(List<RechnungModel> rechnungModelList, boolean forCustomer) {
        logger.debug("Start printing ...");

        JasperPrint jasperPrint;
        JasperPrint jasperThankPrint;
        InputStream rechnungStream = null;
        InputStream rechnungThankStream = null;
        PrintJRDataSourceBase printJRDataSource = null;

        for(RechnungModel model: rechnungModelList){
            if(model.getRechnungType() == RechnungType.RECHNUNG){

                Optional<CustomerModel> customerEntityOptional = RechnungManagerSpringApp
                        .getCustomerService().getById(model.getCustomerId());
                if(customerEntityOptional.isEmpty()){
                    throw new RuntimeException("Der Kunde von der Rechnung nicht gefunden!");
                }

                printJRDataSource = new RechnungPrintJRDataSource(model, customerEntityOptional.get());

                try {
                    rechnungStream = RechnungManagerFxApp.getJasperFilePath("rechnung");
                    rechnungThankStream = RechnungManagerFxApp.getJasperFilePath("rechnung_thank");
                } catch (Exception e) {
                    logger.error("Error in load print report.", e);
                    return;
                }

            }
            if(model.getRechnungType() == RechnungType.QUITTUNG){
                printJRDataSource = new QuittungPrintJRDataSource(model);

                try {
                    rechnungStream = RechnungManagerFxApp.getJasperFilePath("quittung");
                    rechnungThankStream = RechnungManagerFxApp.getJasperFilePath("quittung_thank");
                } catch (Exception e) {
                    logger.error("Error in load print report.", e);
                    return;
                }

            }

            if(forCustomer){
                try {
                    printJRDataSource.reset();
                    Map<String, Object> printParameterMap = printJRDataSource.getPrintParameter();
                    jasperThankPrint = JasperFillManager.fillReport(rechnungThankStream,
                                                                    printParameterMap,
                                                                    printJRDataSource);
                    JasperPrintManager.printReport(jasperThankPrint, false);
                } catch (Exception e) {
                    logger.error("Error in load print report.", e);
                    return;
                }

            }

            try {
                printJRDataSource.reset();
                Map<String, Object> printParameterMap = printJRDataSource.getPrintParameter();
                jasperPrint = JasperFillManager.fillReport(rechnungStream,
                                                           printParameterMap,
                                                           printJRDataSource);

                JasperPrintManager.printReport(jasperPrint, false);
            } catch (Exception e) {
                logger.error("Error in load print report.", e);
                return;
            }

        }


        logger.debug("End printing.");


    }

    public static void printKostenvoranschlagItems(List<KostenvoranschlagModel> KostenvoranschlagModelItems) {
        logger.debug("Start printing ...");

        JasperPrint jasperPrint;
        InputStream printStream;

        for(KostenvoranschlagModel model: KostenvoranschlagModelItems){

            Optional<CustomerModel> customerEntityOptional = RechnungManagerSpringApp
                    .getCustomerService().getById(model.getCustomerId());
            if(customerEntityOptional.isEmpty()){
                throw new RuntimeException("Der Kunde von der Rechnung nicht gefunden!");
            }

            PrintJRDataSourceBase printJRDataSource = new KostenvoranschlagPrintJRDataSource(model, customerEntityOptional.get());

            try {
                printStream = RechnungManagerFxApp.getJasperFilePath("kostenvoranschlag");

            } catch (Exception e) {
                logger.error("Error in load print report.", e);
                return;
            }

            try {
                printJRDataSource.reset();
                Map<String, Object> printParameterMap = printJRDataSource.getPrintParameter();
                jasperPrint = JasperFillManager.fillReport(printStream,
                                                           printParameterMap,
                                                           printJRDataSource);

                JasperPrintManager.printReport(jasperPrint, false);
            } catch (Exception e) {
                logger.error("Error in load print report.", e);
                return;
            }

        }

        logger.debug("End printing.");
    }


}
