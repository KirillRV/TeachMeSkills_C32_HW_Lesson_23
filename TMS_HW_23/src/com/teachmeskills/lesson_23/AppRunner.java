package com.teachmeskills.lesson_23;
/*
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teachmeskills.lesson_23.model.Book;
import jakarta.xml.bind.JAXBContext;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List; */
/**
 * Используя библиотеку Jackson, напишите Java-программу, которая вычитывает
 * JSON-файл(books.json) и сохранит данные в коллекцию Java.
 * <p>
 * Конвертируйте данные из этой коллекции в XML-формат с помощью
 * библиотеки JAXB. Сохраните полученный XML результат в файл.
 * <p>
 * Используя любой парсер(DOM,SAX,StAX) распарсите данные в Java приложении
 * и выведите в консоль информацию о книге с наибольшим количеством страниц.
 */
/*
public class AppRunner {
    public static void main(String[] args) {

        ObjectMapper mapper = new ObjectMapper();
        JAXBContext context;
        DocumentBuilderFactory factory;

        try {
            List<Book> books = mapper.readValue(new File("books.json"),
                    mapper.getTypeFactory().constructCollectionType(List.class, Book.class));
            Book bookWithBiggestPageAmount = books.stream()
                    .max(Comparator.comparingInt(Book::getPages))
                    .orElse(null);
            if (bookWithBiggestPageAmount != null) {
                System.out.println("title -> " + bookWithBiggestPageAmount.getTitle());
                System.out.println("author -> " + bookWithBiggestPageAmount.getAuthor());
                System.out.println("year -> " + bookWithBiggestPageAmount.getYear());
                System.out.println("pages -> " + bookWithBiggestPageAmount.getPages());
                System.out.println("genre -> " + bookWithBiggestPageAmount.getGenre());
            } else {
                System.out.println("Список книг пуст.");
            }
        } catch (IOException e) {
            System.out.println("Error reading books.json file: " + e.getMessage());
        }
    }
}
*/

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teachmeskills.lesson_23.model.Book;
import com.teachmeskills.lesson_23.model.Root;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;
/**
 * Используя библиотеку Jackson, напишите Java-программу, которая вычитывает
 * JSON-файл(books.json) и сохранит данные в коллекцию Java.
 * <p>
 * Конвертируйте данные из этой коллекции в XML-формат с помощью
 * библиотеки JAXB. Сохраните полученный XML результат в файл.
 * <p>
 * Используя любой парсер(DOM,SAX,StAX) распарсите данные в Java приложении
 * и выведите в консоль информацию о книге с наибольшим количеством страниц.
 */
public class AppRunner {

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        JAXBContext context;
        DocumentBuilderFactory factory;

        try {
            List<Book> books = mapper.readValue(new File("books.json"),
                    mapper.getTypeFactory().constructCollectionType(List.class, Book.class));
            Root root = new Root();
            root.setBookList(books);

            context = JAXBContext.newInstance(Root.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(root, new File("books.xml"));

            factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = factory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File("books.xml"));
            doc.getDocumentElement().normalize();

            NodeList bookList = doc.getElementsByTagName("book");
            Book bookWithBiggestPageAmount = null;

            for (int i = 0; i < bookList.getLength(); i++) {
                Element bookElement = (Element) bookList.item(i);
                try {
                    int pages = Integer.parseInt(bookElement.getElementsByTagName("pages").item(0).getTextContent());
                    Book currentBook = new Book();
                    currentBook.setPages(pages);

                    if(bookElement.getElementsByTagName("title").getLength() > 0) currentBook.setTitle(bookElement.getElementsByTagName("title").item(0).getTextContent());
                    if(bookElement.getElementsByTagName("author").getLength() > 0) currentBook.setAuthor(bookElement.getElementsByTagName("author").item(0).getTextContent());
                    if(bookElement.getElementsByTagName("year").getLength() > 0) currentBook.setYear(Integer.parseInt(bookElement.getElementsByTagName("year").item(0).getTextContent()));
                    if(bookElement.getElementsByTagName("genre").getLength() > 0) currentBook.setGenre(bookElement.getElementsByTagName("genre").item(0).getTextContent());

                    if (bookWithBiggestPageAmount == null || pages > bookWithBiggestPageAmount.getPages()) {
                        bookWithBiggestPageAmount = currentBook;
                    }
                } catch (NumberFormatException | NullPointerException e) {
                    System.err.println("Error processing book: " + e.getMessage());

                }
            }

            if (bookWithBiggestPageAmount != null) {
                System.out.println("title -> " + bookWithBiggestPageAmount.getTitle());
                System.out.println("author -> " + bookWithBiggestPageAmount.getAuthor());
                System.out.println("year -> " + bookWithBiggestPageAmount.getYear());
                System.out.println("pages -> " + bookWithBiggestPageAmount.getPages());
                System.out.println("genre -> " + bookWithBiggestPageAmount.getGenre());
            } else {
                System.out.println("The book list is empty or contains errors.");
            }

        } catch (IOException | JAXBException | ParserConfigurationException | SAXException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}