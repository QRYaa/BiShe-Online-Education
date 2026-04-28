import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/bsAppLog/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/bsAppLog/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/bsAppLog/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/bsAppLog/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/bsAppLog/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/bsAppLog/delete',
    data,
  });
}
