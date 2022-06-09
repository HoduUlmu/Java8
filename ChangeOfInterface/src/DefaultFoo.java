public class DefaultFoo implements Foo {

    String name;

    public DefaultFoo(String name) {
        this.name = name;
    }

    @Override
    public void printName() {
        System.out.println(this.name);
    }

    @Override
    public String getName() {
        return this.name;
    }

    // 재정의도 가능
    // Bar,Foo 둘 다 implements 할 경우에는 둘 중 어디에 있는걸 상속받아야 하는지 모르기 때문에 직접 오버라이딩해야함
//    @Override
//    public void printNameUpperCase() {
//        Foo.super.printNameUpperCase();
//    }
}
