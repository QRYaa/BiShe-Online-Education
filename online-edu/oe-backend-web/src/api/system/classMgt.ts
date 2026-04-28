import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/bsClass/page',
    params,
  });
}
export function listAll() {
  return request.get<any>({
    url: '/admin/bsClass/listAll',
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/bsClass/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/bsClass/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/bsClass/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/bsClass/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/bsClass/delete',
    data,
  });
}
