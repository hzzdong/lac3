package test.com.linkallcloud.comm.security.sm;

public class MessageA {
    private Long id;
    private String content;
    private Consumer consumer;

    public MessageA() {
        super();
    }

    public MessageA(Long id, String content, Consumer consumer) {
        super();
        this.id = id;
        this.content = content;
        this.consumer = consumer;
    }

    /**
     * @return the consumer
     */
    public Consumer getConsumer() {
        return consumer;
    }

    /**
     * @param consumer
     *            the consumer to set
     */
    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     *            the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

}
