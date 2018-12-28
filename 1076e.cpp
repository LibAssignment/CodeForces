#include <iostream>
#include <vector>
#include <map>
#include <set>
#include <stack>
#include <memory>

using namespace std;

typedef map<int, vector<pair<int, int>>> update_type;

struct node {
  shared_ptr<node> pl, pr;
  int l, r;
  long long data;
} empty_node;

shared_ptr<node> make_node(int l, int r) {
  // cerr << "making: " << l << ", " << r << endl;
  auto head = make_shared<node>(empty_node);
  head->l = l; head->r = r;
  return head;
}

void update(node& head, int left, int right, int data) {
  // cerr<< "low(" << left << " " << right << "): " << head.l << "," << head.r << "," << data << endl;
  if (head.l == left && head.r == right) { // assert left == head.l && right == head.r
    head.data += data;
    return;
  }
  int m = (head.l + head.r) / 2; // [left, m], [m+1, right]
  if (left <= m) {
    if (!head.pl) head.pl = make_node(left, m);
    head.pl = make_shared<node>(*head.pl);
    update(*head.pl, left, min(m, right), data);
  }
  if (right > m) {
    if (!head.pr) head.pr = make_node(m+1, right);
    head.pr = make_shared<node>(*head.pr);
    update(*head.pr, max(left, m+1), right, data);
  }
}

long long query(const node& head, int k) {
  int m = (head.l + head.r) / 2;
  if (k <= m) {
    if (!head.pl) return head.data;
    return head.data + query(*head.pl, k);
  }
  if (k > m) {
    if (!head.pr) return head.data;
    return head.data + query(*head.pr, k);
  }
  return head.data; // assert false
}

void dump(const node& head) {
  cerr << "(" << head.l << ", " << head.r << "): " << head.data << endl;
  if (head.pl) dump(*head.pl);
  if (head.pr) dump(*head.pr);
}

void do_dfs(vector<long long>& result, int depth, int parent, int now, const node& prev_head,
            const map<int, vector<int>>& edges, const update_type& updates) {
  // cerr << "dfs: " << now << endl;
  // dump(prev_head);
  node head = prev_head;
  for (auto u : updates.at(now)) {
    // cerr << "updating " << depth << depth+u.first << u.second << endl;
    update(head, depth, depth+u.first, u.second);
  }
  for (int v : edges.at(now)) {
    if (v != parent)
      do_dfs(result, depth+1, now, v, head, edges, updates);
  }
  result[now] = query(head, depth);
}

vector<long long> dfs(int n, const map<int, vector<int>>& edges, const update_type& updates) {
  vector<long long> result(n, 0);
  auto head = make_node(0, n);
  do_dfs(result, 0, -1, 0, *head, edges, updates);
  return result;
}

int main() {
  int n; cin>>n;
  map<int, vector<int>> edges;
  for (int i=0; i<n-1; i++) {
    int x,y; cin>>x>>y; x--, y--;
    edges[x].push_back(y);
    edges[y].push_back(x);
  }
  int m; cin>>m;
  update_type updates;
  for (int i=0; i<n; i++) updates[i];
  for (int k=0; k<m; k++) {
    int v,d,x; cin>>v>>d>>x; v--;
    updates[v].push_back({d, x});
  }
  vector<long long> result = dfs(n, edges, updates);
  cout << result[0];
  for (int i=1; i<n; i++) {
    cout << " " << result[i];
  }
  cout << endl;
  return 0;
}