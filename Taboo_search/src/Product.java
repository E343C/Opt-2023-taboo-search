public class Product
{
    private int taboo,w,d,p,id;

    public Product(int id, int w, int d, int p, int taboo)
    {
        this.taboo = taboo;
        this.w = w;
        this.d = d;
        this.p = p;
        this.id=id;
    }

    public int getTaboo() {
        return taboo;
    }

    public int getW() {
        return w;
    }

    public int getD() {
        return d;
    }

    public int getP() {
        return p;
    }

    public int getId() {
        return id;
    }

    public void setTaboo(int taboo) {
        this.taboo = taboo;
    }
}
