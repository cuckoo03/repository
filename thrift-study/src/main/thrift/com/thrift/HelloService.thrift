namespace java com.thrift

service HelloService
{
	string greeting(1:string name, 2:i32 age)
}