import { request } from '@/utils/request';

export function loadAll() {
  return request.get<any>({
    url: '/admin/bsDict/loadAll',
  });
}
export function dictPage(params: any) {
  return request.get<any>({
    url: '/admin/bsDict/page',
    params,
  });
}
export function dictSave(data: any) {
  return request.post<any>({
    url: '/admin/bsDict/save',
    data,
  });
}
export function dictUpdate(data: any) {
  return request.put<any>({
    url: '/admin/bsDict/update',
    data,
  });
}
export function dictGet(id: any) {
  return request.get<any>({
    url: `/admin/bsDict/get/${id}`,
  });
}
export function dictLoad(code: any) {
  return request.get<any>({
    url: `/admin/bsDict/load/${code}`,
  });
}
export function dictDel(id: any) {
  return request.delete<any>({
    url: `/admin/bsDict/delete/${id}`,
  });
}
export function dictBatchDel(data: any) {
  return request.post<any>({
    url: '/admin/bsDict/delete',
    data,
  });
}

// --- dictItem ---
export function itemList(dictId: any) {
  return request.get<any>({
    url: `/admin/bsDict/item/list/${dictId}`,
  });
}

export function itemGet(id: any) {
  return request.get<any>({
    url: `/admin/bsDict/item/get/${id}`,
  });
}

export function itemSave(data: any) {
  return request.post<any>({
    url: '/admin/bsDict/item/save',
    data,
  });
}
export function itemUpdate(data: any) {
  return request.put<any>({
    url: '/admin/bsDict/item/update',
    data,
  });
}

export function itemDel(id: any) {
  return request.delete<any>({
    url: `/admin/bsDict/item/delete/${id}`,
  });
}
