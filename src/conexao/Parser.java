/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexao;


import model.Artigo;
import java.util.ArrayList;
import java.util.Collection;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;
import org.jsoup.Connection;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rhau
 */
public class Parser {
    
    private Collection<Artigo> artigos = new ArrayList<>();
    private Document documentoHtml;
    private Gson gson = new Gson();

    public Parser() {
    }
    
    public Parser(String url) {
        parserUrl(url);
    }
    
    
    public Collection<Artigo> getArtigos() {
        return artigos;
    }

    public void setArtigos(Collection<Artigo> artigos) {
        this.artigos = artigos;
    }

    public Document getDocumentoHtml() {
        return documentoHtml;
    }

    public void setDocumentoHtml(Document documentoHtml) {
        this.documentoHtml = documentoHtml;
    }

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }
    
    public void parserUrl(String url) {
        setHtmlConexao(getConexao(url));
        mapearArtigos(getElementos());
    }
    
    public Connection getConexao(String url) {     
        Connection c = Jsoup.connect(url);
        c.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6");
        c.referrer("http://www.google.com");  
        return c;
    }
    
    public void setHtmlConexao(Connection c) {
        try {
            setDocumentoHtml(c.get());
        } catch (IOException ex) {
            System.out.println("Erro ao obter HTML da conexao: " + ex);
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Elements getElementos() {
        Elements e = getDocumentoHtml().select("#gs_ccl .gs_r .gs_ri");
        return e;
    }
    
    public void mapearArtigos(Elements elementos) {
        for (Element e : elementos) {
            Artigo a = new Artigo();
            a.setTitulo(e.select(".gs_rt a").text());
            a.setResumo(e.select(".gs_rs").text());
            a.setLink(e.select(".gs_rt a").attr("href"));
            a.setCitacoes(e.select(".gs_fl a[href]").text());
            a.setLinkRelacionados(e.select(".gs_fl a").attr("href"));
            artigos.add(a);                                             
            break;
        }
    }
    
    public void imprimirResultados() {
        for (Artigo a : getArtigos()) {
            System.out.println(a.toString());
            System.out.println("------------------\n");
        }
    }
    
}

