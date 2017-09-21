function newt(f, fp, k)
%f=@(x)x^3+x-1;
%fp=@(x) 3*x^2+1;
g=@(x) x-f(x)/fp(x);
steps=k;
x=zeros(steps+1,1);
x(1)=0.5;
for i=1:steps
  x(i+1)=g(x(i))
end
r=x(steps+1)
%e=abs(x-r);
%rat=zeros(steps+1,1);
%for i=1:steps
%  rat(i)=e(i+1)/e(i)^2;
%end
%rat(steps+1)=0;
%[x e rat]
e=abs(x-r);
rat=zeros(steps+1,1);
for i=1:steps
  rat(i)=e(i+1)/e(i);
end
rat(steps+1)=0;
[x e rat]