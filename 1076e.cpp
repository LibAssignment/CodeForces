#include <iostream>
#include <vector>
#include <stack>
#include <memory>

using namespace std;

typedef vector<vector<pair<int, int>>> update_type;
typedef vector<vector<int>> edge_type;
typedef int node_ptr;

const int M = 20*300020;
struct node {
  node_ptr pl, pr;
  int l, r;
  long long data;
} empty_node[M]; // M*log_2(M)*4
// vector<node> empty_node(5*20*300020);

node_ptr make_node(int l, int r) {
  // cerr << "making: " << l << ", " << r << endl;
  static int p = 1;
  if (p >= M) {
    cerr << "overflow" << endl;
  }
  empty_node[p].l = l, empty_node[p].r = r;
  return p++;
}

node& get_node(node_ptr p) {
  return empty_node[p];
}

void update(node& head, int left, int right, int data) {
  // cerr<< "low(" << left << " " << right << "): " << head.l << "," << head.r << "," << data << endl;
  if (left <= head.l && head.r <= right) {
    head.data += data;
    return;
  }
  int m = (head.l + head.r) / 2; // [left, m], [m+1, right]
  if (left <= m) {
    if (!head.pl) head.pl = make_node(head.l, m);
    update(get_node(head.pl), left, right, data);
  }
  if (right > m) {
    if (!head.pr) head.pr = make_node(m+1, head.r);
    update(get_node(head.pr), left, right, data);
  }
}

long long query(const node& head, int k) {
  int m = (head.l + head.r) / 2;
  if (k <= m) {
    if (!head.pl) return head.data;
    return head.data + query(get_node(head.pl), k);
  }
  if (k > m) {
    if (!head.pr) return head.data;
    return head.data + query(get_node(head.pr), k);
  }
  return head.data; // assert false
}

void dump(const node& head) {
  cerr << "(" << head.l << ", " << head.r << "): " << head.data << endl;
  if (head.pl) dump(get_node(head.pl));
  if (head.pr) dump(get_node(head.pr));
}

void do_dfs(vector<long long>& result, int depth, int parent, int now, node& head,
            const edge_type& edges, const update_type& updates) {
  // cerr << "dfs: " << now << endl;
  // dump(head);
  for (auto u : updates.at(now)) {
    // cerr << "updating " << depth << " " << depth+u.first << " " << u.second << endl;
    update(head, depth, depth+u.first, u.second);
  }
  result[now] = query(head, depth);
  for (int v : edges.at(now)) {
    if (v != parent)
      do_dfs(result, depth+1, now, v, head, edges, updates);
  }
  for (auto u : updates.at(now)) {
    // cerr << "updating " << depth << " " << depth+u.first << " " << u.second << endl;
    update(head, depth, depth+u.first, -u.second);
  }
}

vector<long long> dfs(int n, const edge_type& edges, const update_type& updates) {
  vector<long long> result(n, 0);
  node_ptr head = make_node(0, n);
  do_dfs(result, 0, -1, 0, get_node(head), edges, updates);
  return result;
}

int main() {
  int n; scanf("%d", &n);
  edge_type edges(n);
  for (int i=0; i<n-1; i++) {
    int x,y; scanf("%d%d", &x, &y); x--, y--;
    edges[x].push_back(y);
    edges[y].push_back(x);
  }
  int m; scanf("%d", &m);
  update_type updates(n);
  for (int k=0; k<m; k++) {
    int v,d,x; scanf("%d%d%d", &v, &d, &x); v--;
    updates[v].push_back({d, x});
  }
  vector<long long> result = dfs(n, edges, updates);
  printf("%lld", result[0]);
  for (int i=1; i<n; i++) {
    printf(" %lld", result[i]);
  }
  printf("\n");
  return 0;
}
