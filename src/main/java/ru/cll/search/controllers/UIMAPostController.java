package ru.cll.search.controllers;

import org.apache.uima.UIMAException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.SAXException;
import ru.cll.search.service.UIMAService;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * author: Nikita
 * since: 05.05.2014
 */
@Controller
public class UIMAPostController extends BaseController{
    @Autowired
    private UIMAService uimaService;

    @RequestMapping(value = "/getDirtyAnnotations", method = {RequestMethod.GET, RequestMethod.POST})
    public String getAllAnnotation(@RequestParam String text) throws IOException, UIMAException {
        request.setAttribute("initialText", text);
        String annotationInfo = uimaService.getAllAnnotationsAsString(text);
        request.setAttribute("annotations", annotationInfo.replace("\n", "<br>"));
        return "templates/minimalUimaTemplate";
    }

    @RequestMapping(value = "/getXmi", method = {RequestMethod.GET, RequestMethod.POST})
    public HttpEntity<byte[]> getJCasXmiWay(@RequestParam String text)
            throws UIMAException, SAXException, IOException {
        String xml = uimaService.getXmlTranslatedResult(text);

        // Dirty way to return xml without marshalling view
        byte[] documentBody = xml.getBytes();

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "xml"));
        header.setContentLength(documentBody.length);
        return new HttpEntity<byte[]>(documentBody, header);
    }
}
