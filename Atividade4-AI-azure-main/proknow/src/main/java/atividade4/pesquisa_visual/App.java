package atividade4.pesquisa_visual;

import com.azure.ai.vision.imageanalysis.ImageAnalysisClient;
import com.azure.ai.vision.imageanalysis.ImageAnalysisClientBuilder;
import com.azure.ai.vision.imageanalysis.models.DetectedPerson;
import com.azure.ai.vision.imageanalysis.models.ImageAnalysisResult;
import com.azure.ai.vision.imageanalysis.models.VisualFeatures;
import com.azure.core.credential.KeyCredential;
import com.azure.core.util.BinaryData;
import java.io.File;
import java.util.Arrays;

public class App {
    public static void main(String[] args) {
        
        //Variavéis de Ambiente com os dados para o serviço do Azure
        String endpoint = System.getenv("VISION_ENDPOINT");
        String key = System.getenv("VISION_KEY");

        if(endpoint == null || key == null){
            System.out.println("Endpoint ou Chave não informados corretamente.");
            System.exit(1);
        }else{
            System.out.println("Credências aprovadas!");
        }

        //Inicialização do cliente de comunicação com o serviço do AZURE
        ImageAnalysisClient client = new ImageAnalysisClientBuilder().endpoint(endpoint).credential(new KeyCredential(key)).buildClient();

        try{

            ImageAnalysisResult result1 = client.analyze(
                BinaryData.fromFile(new File("proknow\\src\\main\\resources\\img\\imagem_com_pessoas.png").toPath()),
                Arrays.asList(VisualFeatures.PEOPLE),
                null);

            ImageAnalysisResult result2 = client.analyze(
                BinaryData.fromFile(new File("proknow\\src\\main\\resources\\img\\IMG_1826.jpg").toPath()),
                Arrays.asList(VisualFeatures.PEOPLE),
                null);
            
            //Resultado da imagem com pessoas!
            printResults(result1);

            //Resultado da imagem sem pessoas!
            printResults(result2);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void printResults(ImageAnalysisResult result) {

        System.out.println("Resultados da imagem analisada:");
        System.out.println(" Pessoas:");
        for (DetectedPerson person : result.getPeople().getValues()) {

            
            if(person.getConfidence() >= 0.5){
                System.out.println("   Posição "
                    + person.getBoundingBox() + ", Precisão " + String.format("%.4f", person.getConfidence()));
            }
        }
        System.out.println(" Altura da Imagem = " + result.getMetadata().getHeight());
        System.out.println(" Largura da Imagem = " + result.getMetadata().getWidth());
        System.out.println(" Versão do Serviço de Visão Computacional = " + result.getModelVersion());
    }
}