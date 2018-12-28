#include <iostream>

using namespace std;

int main() {
  int n = 300000;
  cout << n << endl;
  for (int i = 1; i < n; i++) {
    cout << i+1 << " " << rand()%i+1 << endl;
  }
  int m = n;
  cout << m << endl;
  for (int i = 0; i < m; i++) {
    cout << rand()%n+1 << " " << rand()%n*2/3 << " " << rand() << endl;
  }
}
