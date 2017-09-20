function fpi(g, x0, k)
steps=k;
x=zeros(steps+1,1);
x(1)=x0;
for i=1:steps
  x(i+1)=g(x(i));
end
r=x(steps+1)
e=abs(x-r);
rat=zeros(steps+1,1);
for i=1:steps
  rat(i)=e(i+1)/e(i);
end
rat(steps+1)=0;
[x e rat]