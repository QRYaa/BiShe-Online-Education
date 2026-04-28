import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/oeNote/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/oeNote/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/oeNote/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/oeNote/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/oeNote/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/oeNote/delete',
    data,
  });
}

export function switchEnable(data: any) {
  return request.put<any>({
    url: '/admin/oeNote/switchEnable',
    data,
  });
}
